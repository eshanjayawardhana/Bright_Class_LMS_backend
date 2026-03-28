package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.EnrollmentRequest;
import com.lms.lms_backend.dto.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enroll(EnrollmentRequest enrollmentRequest, String studentEmail);
    List<EnrollmentResponse> getMyEnrollments(String studentEmail);
    List<EnrollmentResponse> getPendingEnrollments();
    EnrollmentResponse approve(Long id);
    EnrollmentResponse reject(Long id);

}
