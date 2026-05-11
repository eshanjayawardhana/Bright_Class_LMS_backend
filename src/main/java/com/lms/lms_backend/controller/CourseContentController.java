package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.ApiResponse;
import com.lms.lms_backend.dto.CourseContentRequestDTO;
import com.lms.lms_backend.dto.CourseContentResponseDTO;
import com.lms.lms_backend.service.CourseContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class CourseContentController {

    private final CourseContentService courseContentService;

    public CourseContentController(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    // 👨‍🏫 LECTURER
    @PostMapping
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<ApiResponse<CourseContentResponseDTO>> create(
            @RequestBody CourseContentRequestDTO request,
            Authentication auth) {

        CourseContentResponseDTO response = courseContentService.createContent(request, auth.getName());

        return new ResponseEntity<>(
                ApiResponse.success("Course content created successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 🎓 STUDENT
    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseContentResponseDTO>>> getContent(
            @PathVariable Long courseId,
            Authentication auth) {

        List<CourseContentResponseDTO> response = courseContentService.getCourseContent(courseId, auth.getName());
        return ResponseEntity.ok(
                ApiResponse.success("Content loaded successfully for student", response, 200)
        );
    }

    // LECTURER & ADMIN
    @GetMapping("/manage/{courseId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CourseContentResponseDTO>>> getContentForManagement(
            @PathVariable Long courseId) {

        List<CourseContentResponseDTO> response = courseContentService.getContentForLecturerOrAdmin(courseId);
        return ResponseEntity.ok(
                ApiResponse.success("Content loaded successfully", response, 200)
        );
    }

    // LECTURER & ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteContent(@PathVariable Long id) {
        courseContentService.deleteContent(id);
        return ResponseEntity.ok(
                ApiResponse.success("Course content deleted successfully", null, 200)
        );
    }
}
