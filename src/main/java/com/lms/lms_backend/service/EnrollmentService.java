package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.EnrollmentRequestDTO;
import com.lms.lms_backend.dto.EnrollmentResponseDTO;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponseDTO enroll(EnrollmentRequestDTO enrollmentRequestDTO, String studentEmail);
    List<EnrollmentResponseDTO> getMyEnrollments(String studentEmail);
    List<EnrollmentResponseDTO> getPendingEnrollments();
    EnrollmentResponseDTO approve(Long id);
    EnrollmentResponseDTO reject(Long id);
    List<EnrollmentResponseDTO> getAllEnrollments(String search, EnrollmentStatus status);

}
