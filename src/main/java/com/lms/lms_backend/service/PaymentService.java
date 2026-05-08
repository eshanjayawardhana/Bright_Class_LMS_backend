package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO request);

    PaymentResponseDTO verifyPayment(Long id);

    PaymentResponseDTO rejectPayment(Long id,String reason);

    PaymentResponseDTO uploadSlip(MultipartFile file, Long enrollmentId);

    List<PaymentResponseDTO> getAllPayments(String search, PaymentStatus status);
    PaymentResponseDTO getPaymentById(Long id);
}
