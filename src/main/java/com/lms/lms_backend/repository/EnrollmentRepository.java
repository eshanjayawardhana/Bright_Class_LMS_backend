package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status);
}
