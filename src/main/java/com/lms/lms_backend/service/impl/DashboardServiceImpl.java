package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.AdminDashboardDTO;
import com.lms.lms_backend.dto.EnrollmentResponseDTO;
import com.lms.lms_backend.dto.LecturerDashboardDTO;
import com.lms.lms_backend.dto.StudentDashboardDTO;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.EnrollmentMapper;
import com.lms.lms_backend.repository.*;
import com.lms.lms_backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseContentRepository contentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(UserRepository userRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, CourseContentRepository contentRepository, EnrollmentMapper enrollmentMapper, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.contentRepository = contentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public AdminDashboardDTO getAdminDashboard() {
        return AdminDashboardDTO.builder()
                .totalUsers(userRepository.count())
                .totalCourses(courseRepository.count())

                .totalEnrollments(enrollmentRepository.countByStatus(EnrollmentStatus.APPROVED))
                .pendingEnrollments(enrollmentRepository.countByStatus(EnrollmentStatus.PENDING))
                .rejectEnrollments(enrollmentRepository.countByStatus(EnrollmentStatus.REJECTED))

                .totalPayments(paymentRepository.countByStatus(PaymentStatus.VERIFIED))
                .pendingPayments(paymentRepository.countByStatus(PaymentStatus.PENDING))
                .rejectPayments(paymentRepository.countByStatus(PaymentStatus.REJECTED))

                .build();
    }

    @Override
    public StudentDashboardDTO getStudentDashboard(String email) {
        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // All registered students
        List<EnrollmentResponseDTO> activeEnrollments = enrollmentRepository
                .findByStudentIdAndStatus(student.getId(), EnrollmentStatus.APPROVED)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());

        return StudentDashboardDTO.builder()
                .approvedCoursesCount(enrollmentRepository.countByStudentIdAndStatus(student.getId(), EnrollmentStatus.APPROVED))
                .pendingCoursesCount(enrollmentRepository.countByStudentIdAndStatus(student.getId(), EnrollmentStatus.PENDING))
                .rejectedCoursesCount(enrollmentRepository.countByStudentIdAndStatus(student.getId(), EnrollmentStatus.REJECTED))
                .myEnrollments(activeEnrollments)
                .build();
    }

    @Override
    public LecturerDashboardDTO getInstructorDashboard(String email) {
        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        return LecturerDashboardDTO.builder()
                .totalUploadedContent(contentRepository.countByLecturerId(instructor.getId()))
                .myCoursesCount(courseRepository.countByLecturerId(instructor.getId()))
                .build();
    }
}
