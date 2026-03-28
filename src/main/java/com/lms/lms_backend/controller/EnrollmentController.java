package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.EnrollmentRequest;
import com.lms.lms_backend.dto.EnrollmentResponse;
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
    public ResponseEntity<EnrollmentResponse> enroll(@RequestBody EnrollmentRequest enrollmentRequest, Authentication authentication){
        EnrollmentResponse response = enrollmentService.enroll(enrollmentRequest, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 🎓 STUDENT
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> myEnrollments(Authentication auth) {
        List<EnrollmentResponse> response = enrollmentService.getMyEnrollments(auth.getName());
        return ResponseEntity.ok(response);
    }

    // 👨‍💼 ADMIN
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> pending() {
        List<EnrollmentResponse> responses = enrollmentService.getPendingEnrollments();
        return ResponseEntity.ok(responses);
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> approve(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.approve(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> reject(@PathVariable Long id) {
        EnrollmentResponse response = enrollmentService.reject(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
