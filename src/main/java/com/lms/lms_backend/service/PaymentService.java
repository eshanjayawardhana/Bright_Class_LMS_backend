package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO request);

    PaymentResponseDTO verifyPayment(Long id);

    PaymentResponseDTO rejectPayment(Long id);

    PaymentResponseDTO uploadSlip(MultipartFile file, Long enrollmentId);
}
