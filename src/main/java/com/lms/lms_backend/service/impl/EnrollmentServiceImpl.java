package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.EnrollmentRequest;
import com.lms.lms_backend.dto.EnrollmentResponse;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.EnrollmentMapper;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public EnrollmentResponse enroll(EnrollmentRequest enrollmentRequest, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail).orElseThrow(()-> new RuntimeException("User not found"));
        Course course = courseRepository.findById(enrollmentRequest.getCourseId()).orElseThrow(()-> new RuntimeException("Course not found"));

        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentRequest,studentEmail,student,course);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(saved);
    }

    @Override
    public List<EnrollmentResponse> getMyEnrollments(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponse> getPendingEnrollments() {
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentResponse approve(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.APPROVED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toDto(updatedEnrollment);
    }

    @Override
    public EnrollmentResponse reject(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.REJECTED);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toDto(updatedEnrollment);
    }
}
