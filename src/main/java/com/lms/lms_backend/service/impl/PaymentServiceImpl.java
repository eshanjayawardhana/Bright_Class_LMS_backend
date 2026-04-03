package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Payment;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.entity.enums.PaymentMethod;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import com.lms.lms_backend.exception.FileStorageException;
import com.lms.lms_backend.exception.InvalidOperationException;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.PaymentMapper;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.PaymentRepository;
import com.lms.lms_backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

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

    // YAML එකෙන් path එක මෙතනට ගන්නවා
    @Value("${file.upload-dir}")
    private String uploadDir;

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

    @Override
    public PaymentResponseDTO uploadSlip(MultipartFile file, Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file.");
            }

            // 1. File Name (Security Check)
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            if (originalFilename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFilename);
            }

            // 2. Create Unique name for Duplicate names
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // 3. Set path to stored file
            Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(uniqueFileName);

            // If not folder then create that
            Files.createDirectories(targetLocation.getParent());

            // Copy the file
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // check weather already payment has or not to enrollment
            Payment payment = paymentRepository.findByEnrollmentId(enrollmentId).orElse(null);

            if (payment != null) {
                // if already payment has updated the payment
                if (payment.getStatus() == PaymentStatus.VERIFIED) {
                    throw new InvalidOperationException("This payment is already verified. You cannot upload a new slip.");
                }
                payment.setSlipUrl("/uploads/payments/" + uniqueFileName);
                payment.setStatus(PaymentStatus.PENDING);
                payment.setPaymentDate(LocalDateTime.now());
            } else {
                // if not payment then create a new payment
                payment = Payment.builder()
                        .enrollment(enrollment)
                        .paymentMethod(PaymentMethod.SLIP)
                        .slipUrl("/uploads/payments/" + uniqueFileName)
                        .amount(0.0)
                        .paymentDate(LocalDateTime.now())
                        .status(PaymentStatus.PENDING)
                        .build();
            }

            Payment savedPayment = paymentRepository.save(payment);
            return paymentMapper.toDto(savedPayment);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }
}

