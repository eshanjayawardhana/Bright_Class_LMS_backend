package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.ApiResponse;
import com.lms.lms_backend.dto.EnrollmentRequestDTO;
import com.lms.lms_backend.dto.EnrollmentResponseDTO;
import com.lms.lms_backend.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // 🎓 STUDENT
    @PostMapping("/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> enroll(@RequestBody EnrollmentRequestDTO enrollmentRequestDTO, Authentication authentication){
        EnrollmentResponseDTO response = enrollmentService.enroll(enrollmentRequestDTO, authentication.getName());

        return new ResponseEntity<>(
                ApiResponse.success("Enrollment request created successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 🎓 STUDENT
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> myEnrollments(Authentication auth) {
        List<EnrollmentResponseDTO> response = enrollmentService.getMyEnrollments(auth.getName());

        return ResponseEntity.ok(
                ApiResponse.success("My Enrollments", response, 200)
        );
    }

    // 👨‍💼 ADMIN
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDTO>>> pending() {
        List<EnrollmentResponseDTO> response = enrollmentService.getPendingEnrollments();

        return ResponseEntity.ok(
                ApiResponse.success("Pending Enrollments", response, 200)
        );
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> approve(@PathVariable Long id) {
        EnrollmentResponseDTO response = enrollmentService.approve(id);

        return new ResponseEntity<>(
                ApiResponse.success("Enrollment approve successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EnrollmentResponseDTO>> reject(@PathVariable Long id) {
        EnrollmentResponseDTO response = enrollmentService.reject(id);
        return new ResponseEntity<>(
                ApiResponse.success("Enrollment reject successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }
}
