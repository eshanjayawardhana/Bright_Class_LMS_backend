package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    // check weather a student approved for course
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, EnrollmentStatus status);

    long countByStatus(EnrollmentStatus status);
    long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
    List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e WHERE " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.course.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Enrollment> searchEnrollments(@Param("search") String search, @Param("status") EnrollmentStatus status);
}
