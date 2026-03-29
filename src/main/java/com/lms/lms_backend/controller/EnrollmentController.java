package com.lms.lms_backend.controller;

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
    public ResponseEntity<EnrollmentResponseDTO> enroll(@RequestBody EnrollmentRequestDTO enrollmentRequestDTO, Authentication authentication){
        EnrollmentResponseDTO response = enrollmentService.enroll(enrollmentRequestDTO, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 🎓 STUDENT
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponseDTO>> myEnrollments(Authentication auth) {
        List<EnrollmentResponseDTO> response = enrollmentService.getMyEnrollments(auth.getName());
        return ResponseEntity.ok(response);
    }

    // 👨‍💼 ADMIN
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentResponseDTO>> pending() {
        List<EnrollmentResponseDTO> responses = enrollmentService.getPendingEnrollments();
        return ResponseEntity.ok(responses);
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponseDTO> approve(@PathVariable Long id) {
        EnrollmentResponseDTO response = enrollmentService.approve(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponseDTO> reject(@PathVariable Long id) {
        EnrollmentResponseDTO response = enrollmentService.reject(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
