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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/content")
public class CourseContentController {

    private final CourseContentService courseContentService;

    public CourseContentController(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    // LECTURER
//    @PostMapping
//    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
//    public ResponseEntity<ApiResponse<CourseContentResponseDTO>> create(
//            @RequestBody CourseContentRequestDTO request,
//            Authentication auth) {
//
//        CourseContentResponseDTO response = courseContentService.createContent(request, auth.getName());
//
//        return new ResponseEntity<>(
//                ApiResponse.success("Course content created successfully", response, HttpStatus.CREATED.value()), // 201
//                HttpStatus.CREATED
//        );
//    }

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseContentResponseDTO>> createContent(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("contentType") String contentType,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "scheduledTime", required = false) String scheduledTime,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Authentication auth) {

        CourseContentRequestDTO request = new CourseContentRequestDTO();
        request.setTitle(title);
        request.setDescription(description);
        request.setContentType(com.lms.lms_backend.entity.enums.ContentType.valueOf(contentType));
        request.setCourseId(courseId);
        request.setUrl(url);

        if (scheduledTime != null && !scheduledTime.trim().isEmpty()) {
            request.setScheduledTime(java.time.LocalDateTime.parse(scheduledTime));
        }

        CourseContentResponseDTO response = courseContentService.createContentWithFile(request, files, auth.getName());

        return new ResponseEntity<>(
                ApiResponse.success("Course content uploaded successfully", response, HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }

    // STUDENT
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

    @GetMapping("/edit/{contentId}")
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseContentResponseDTO>> getContentById(@PathVariable Long contentId) {
        CourseContentResponseDTO response = courseContentService.getContentById(contentId);
        return ResponseEntity.ok(ApiResponse.success("Content fetched successfully", response,200));
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

    @PutMapping(value = "/{contentId}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('LECTURER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CourseContentResponseDTO>> updateContent(
            @PathVariable Long contentId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "scheduledTime", required = false) String scheduledTime,
            @RequestParam(value = "files", required = false) java.util.List<org.springframework.web.multipart.MultipartFile> files,
            @RequestParam(value = "deletedAttachmentIds", required = false) java.util.List<Long> deletedAttachmentIds,
            Authentication auth) {

        CourseContentRequestDTO request = new CourseContentRequestDTO();
        request.setTitle(title);
        request.setDescription(description);
        request.setUrl(url);

        if (scheduledTime != null && !scheduledTime.trim().isEmpty()) {
            request.setScheduledTime(java.time.LocalDateTime.parse(scheduledTime));
        }

        CourseContentResponseDTO response = courseContentService.updateContentWithFiles(
                contentId, request, files, deletedAttachmentIds, auth.getName()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Course content updated successfully", response, 200)
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
