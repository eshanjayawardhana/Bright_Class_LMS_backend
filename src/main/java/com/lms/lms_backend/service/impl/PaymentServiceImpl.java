package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Payment;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.PaymentMapper;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.PaymentRepository;
import com.lms.lms_backend.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, EnrollmentRepository enrollmentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        Payment payment = paymentMapper.toEntity(request,enrollment);
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO verifyPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.VERIFIED);
        paymentRepository.save(payment);

        Enrollment enrollment = payment.getEnrollment();
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        enrollmentRepository.save(enrollment);

        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDTO rejectPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        // 1. Payment REJECTED
        payment.setStatus(PaymentStatus.REJECTED);
        paymentRepository.save(payment);

        // 2. Enrollment REJECTED
        Enrollment enrollment = payment.getEnrollment();
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollmentRepository.save(enrollment);

        return paymentMapper.toDto(payment);
    }
}

