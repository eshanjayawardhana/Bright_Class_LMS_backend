package com.lms.lms_backend.controller;

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
    public ResponseEntity<CourseContentResponseDTO> create(
            @RequestBody CourseContentRequestDTO request,
            Authentication auth) {

        CourseContentResponseDTO response = courseContentService.createContent(request, auth.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 🎓 STUDENT
    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseContentResponseDTO>> getContent(
            @PathVariable Long courseId,
            Authentication auth) {

        List<CourseContentResponseDTO> response = courseContentService.getCourseContent(courseId, auth.getName());
        return ResponseEntity.ok(response);
    }
}
