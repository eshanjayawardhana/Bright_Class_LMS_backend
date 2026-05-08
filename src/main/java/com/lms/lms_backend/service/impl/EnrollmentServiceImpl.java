package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.EnrollmentRequestDTO;
import com.lms.lms_backend.dto.EnrollmentResponseDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.EnrollmentMapper;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.EmailService;
import com.lms.lms_backend.service.EnrollmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final EmailService emailService;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository, EnrollmentMapper enrollmentMapper, EmailService emailService) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.emailService = emailService;
    }

    @Override
    public EnrollmentResponseDTO enroll(EnrollmentRequestDTO enrollmentRequestDTO, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail).orElseThrow(()-> new RuntimeException("User not found"));
        Course course = courseRepository.findById(enrollmentRequestDTO.getCourseId()).orElseThrow(()-> new RuntimeException("Course not found"));

        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentRequestDTO,studentEmail,student,course);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(saved);
    }

    @Override
    public List<EnrollmentResponseDTO> getMyEnrollments(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getPendingEnrollments() {
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentResponseDTO approve(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.APPROVED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // 📧 Enrollment Approval HTML Template
        String enrollmentApprovalHtml = "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 12px; background-color: #ffffff;\">" +
                "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                "<h1 style=\"color: #1a73e8; margin: 0;\">BrightClass LMS</h1>" +
                "<p style=\"color: #5f6368; font-size: 14px;\">Empowering Your Learning Journey</p>" +
                "</div>" +
                "<div style=\"background-color: #e8f0fe; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 20px;\">" +
                "<h2 style=\"color: #1967d2; margin: 0;\">Enrollment Confirmed! 🎓</h2>" +
                "</div>" +
                "<p style=\"font-size: 16px; color: #3c4043;\">Dear <strong>" + enrollment.getFullName() + "</strong>,</p>" +
                "<p style=\"font-size: 15px; color: #5f6368; line-height: 1.6;\">We are pleased to inform you that your enrollment request for the course <strong>" + enrollment.getCourse().getTitle() + "</strong> has been reviewed and <span style=\"color: #188038; font-weight: bold;\">APPROVED</span> by the administration.</p>" +
                "<div style=\"background-color: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px dashed #dadce0; margin: 20px 0;\">" +
                "<h4 style=\"margin-top: 0; color: #202124;\">Next Steps:</h4>" +
                "<p style=\"margin-bottom: 10px; color: #5f6368;\">You can now access your dashboard to view course contents, join live MS Teams sessions, and watch recorded lessons.</p>" +
                "<a href=\"http://localhost:4200/login\" style=\"display: inline-block; padding: 12px 24px; background-color: #1a73e8; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold; margin-top: 10px;\">Go to Student Dashboard</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; color: #5f6368;\">If you have any questions, feel free to reach out to your lecturer or the support team.</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #eee; margin: 20px 0;\">" +
                "<p style=\"font-size: 13px; color: #9aa0a6; text-align: center;\">Best Regards,<br><strong>BrightClass Administration</strong></p>" +
                "</div>";

        emailService.sendEmail(
                enrollment.getEmail(),
                "Enrollment Confirmed: " + enrollment.getCourse().getTitle(),
                enrollmentApprovalHtml
        );

        return enrollmentMapper.toDto(updatedEnrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponseDTO reject(Long id, String reason) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.REJECTED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        // Logic for default reason if the provided reason is null or empty
        String finalReason = (reason == null || reason.trim().isEmpty())
                ? "Your application did not meet the necessary requirements or contained incorrect information at this time."
                : reason;

        // 📧 Enrollment Rejection HTML Template
        String enrollmentRejectionHtml = "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 12px; background-color: #ffffff;\">" +
                "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                "<h1 style=\"color: #d93025; margin: 0;\">BrightClass LMS</h1>" +
                "</div>" +
                "<div style=\"background-color: #fce8e6; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 20px;\">" +
                "<h2 style=\"color: #c5221f; margin: 0;\">Enrollment REJECTED</h2>" +
                "</div>" +
                "<p style=\"font-size: 16px; color: #3c4043;\">Dear <strong>" + enrollment.getFullName() + "</strong>,</p>" +
                "<p style=\"font-size: 15px; color: #5f6368; line-height: 1.6;\">Thank you for your interest in the course <strong>" + enrollment.getCourse().getTitle() + "</strong>.</p>" +
                "<p style=\"font-size: 15px; color: #5f6368; line-height: 1.6;\">After careful review, we regret to inform you that your enrollment request has been <span style=\"color: #d93025; font-weight: bold;\">REJECTED</span> at this time.</p>" +

                // --- DYNAMIC REASON SECTION ---
                "<div style=\"background-color: #f8f9fa; padding: 15px; border-left: 4px solid #d93025; margin: 20px 0;\">" +
                "<p style=\"margin: 0; color: #202124; font-weight: bold;\">Reason for Rejection:</p>" +
                "<p style=\"margin: 5px 0 0 0; color: #5f6368;\">" + finalReason + "</p>" +
                "</div>" +

                "<p style=\"font-size: 14px; color: #5f6368;\">For further clarification regarding this decision, please contact the administration office.</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #eee; margin: 20px 0;\">" +
                "<p style=\"font-size: 13px; color: #9aa0a6; text-align: center;\">Best Regards,<br><strong>BrightClass Administration</strong></p>" +
                "</div>";

        emailService.sendEmail(
                enrollment.getEmail(),
                "Update Regarding Your Enrollment Request",
                enrollmentRejectionHtml
        );

        return enrollmentMapper.toDto(updatedEnrollment);
    }

    @Override
    public List<EnrollmentResponseDTO> getAllEnrollments(String search, EnrollmentStatus status) {
        return enrollmentRepository.searchEnrollments(search, status)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
