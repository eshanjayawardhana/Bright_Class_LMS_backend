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
import com.lms.lms_backend.service.EmailService;
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
    private final EmailService emailService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, EnrollmentRepository enrollmentRepository, PaymentMapper paymentMapper, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentMapper = paymentMapper;
        this.emailService = emailService;
    }

    // YML path
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

        // HTML Email Template for APPROVAL
        String approvalHtmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;\">" +
                "<h2 style=\"color: #2e7d32; text-align: center;\">BrightClass LMS</h2>" +
                "<div style=\"background-color: #f1f8e9; padding: 15px; border-radius: 5px; text-align: center;\">" +
                "<h3 style=\"color: #1b5e20; margin: 0;\">Payment Verified & Enrollment Approved! 🎉</h3>" +
                "</div>" +
                "<p>Dear <strong>" + enrollment.getFullName() + "</strong>,</p>" +
                "<p>We are thrilled to inform you that your payment for <strong>" + enrollment.getCourse().getTitle() + "</strong> has been successfully verified.</p>" +
                "<p>Your enrollment is now fully approved, and you have complete access to all course materials, live sessions, and recorded videos.</p>" +
                "<div style=\"background-color: #f9f9f9; padding: 15px; border-left: 4px solid #2e7d32; margin: 20px 0;\">" +
                "<h4 style=\"margin-top: 0; color: #333;\">Login Instructions:</h4>" +
                "<ul style=\"margin-bottom: 0; color: #555;\">" +
                "<li><strong>Login Portal:</strong> <a href=\"http://localhost:4200/login\">Access LMS Here</a></li>" +
                "<li><strong>Email ID:</strong> " + enrollment.getEmail() + "</li>" +
                "<li><strong>Password:</strong> Use the password you created during registration.</li>" +
                "</ul>" +
                "</div>" +
                "<p>If you encounter any issues logging in, please contact our support team immediately.</p>" +
                "<p>Best Regards,<br><strong>BrightClass Administration Team</strong></p>" +
                "</div>";

        emailService.sendEmail(
                enrollment.getEmail(),
                "BrightClass LMS - Enrollment Approved! Welcome Aboard",
                approvalHtmlContent
        );

        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional
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

        // HTML Email Template for REJECTION
        String rejectionHtmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;\">" +
                "<h2 style=\"color: #c62828; text-align: center;\">BrightClass LMS</h2>" +
                "<div style=\"background-color: #ffebee; padding: 15px; border-radius: 5px; text-align: center;\">" +
                "<h3 style=\"color: #b71c1c; margin: 0;\">Action Required: Payment Verification Failed ⚠️</h3>" +
                "</div>" +
                "<p>Dear <strong>" + enrollment.getFullName() + "</strong>,</p>" +
                "<p>We regret to inform you that we could not verify the payment slip you uploaded for the course: <strong>" + enrollment.getCourse().getTitle() + "</strong>.</p>" +
                "<p>As a result, your enrollment request is currently marked as <strong style=\"color: #c62828;\">REJECTED</strong>.</p>" +
                "<div style=\"background-color: #f9f9f9; padding: 15px; border-left: 4px solid #c62828; margin: 20px 0;\">" +
                "<h4 style=\"margin-top: 0; color: #333;\">Common Reasons for Rejection:</h4>" +
                "<ul style=\"margin-bottom: 0; color: #555;\">" +
                "<li>The uploaded image was blurry or unreadable.</li>" +
                "<li>The payment amount did not match the course fee.</li>" +
                "<li>The reference number was missing or invalid.</li>" +
                "</ul>" +
                "</div>" +
                "<p><strong>What should you do next?</strong></p>" +
                "<p>Please log in to your dashboard and upload a clear, valid payment slip again. If you believe this is a mistake, please reply to this email or contact our support hotline at 011-XXXXXXX.</p>" +
                "<p>Best Regards,<br><strong>BrightClass Administration Team</strong></p>" +
                "</div>";

        emailService.sendEmail(
                enrollment.getEmail(),
                "BrightClass LMS - Action Required: Payment Verification Failed",
                rejectionHtmlContent
        );

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

