package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    // check weather a student approved for course
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    long countByStatus(EnrollmentStatus status);
    long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
}
