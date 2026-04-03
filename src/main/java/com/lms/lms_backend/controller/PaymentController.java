package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 🎓 STUDENT
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDTO> verify(@PathVariable Long id) {
        PaymentResponseDTO response = paymentService.verifyPayment(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDTO> reject(@PathVariable Long id) {
        PaymentResponseDTO response = paymentService.rejectPayment(id);
        return ResponseEntity.ok(response);
    }

    // 🎓 STUDENT - Upload Slip
    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponseDTO> uploadSlip(
            @RequestParam("file") MultipartFile file,
            @RequestParam("enrollmentId") Long enrollmentId) {

        PaymentResponseDTO response = paymentService.uploadSlip(file, enrollmentId);
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.CREATED);
    }
}

