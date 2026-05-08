package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.ApiResponse;
import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import com.lms.lms_backend.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> create(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createPayment(request);

        return new ResponseEntity<>(
                ApiResponse.success("Payment request created successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> verify(@PathVariable Long id) {
        PaymentResponseDTO response = paymentService.verifyPayment(id);

        return ResponseEntity.ok(
                ApiResponse.success("Payment verified successfully", response, 200)
        );

    }

    // 👨‍💼 ADMIN
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> reject(@PathVariable Long id, @RequestBody(required = false) java.util.Map<String, String> request) {
        String reason = (request != null) ? request.get("reason") : null;
        PaymentResponseDTO response = paymentService.rejectPayment(id, reason);
        return ResponseEntity.ok(
                ApiResponse.success("Payment reject successfully", response, 200)
        );    }

    // 🎓 STUDENT - Upload Slip
    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> uploadSlip(
            @RequestParam("file") MultipartFile file,
            @RequestParam("enrollmentId") Long enrollmentId) {

        PaymentResponseDTO response = paymentService.uploadSlip(file, enrollmentId);

        return new ResponseEntity<>(
                ApiResponse.success("Slip uploaded successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 👨‍💼 ADMIN
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PaymentStatus status) {
        List<PaymentResponseDTO> response = paymentService.getAllPayments(search, status);
        return ResponseEntity.ok(
                ApiResponse.success("All Payments", response, 200)
        );
    }

    // 👨‍💼 ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> getById(@PathVariable Long id) {
        PaymentResponseDTO response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Payment details retrieved", response, 200)
        );
    }
}

