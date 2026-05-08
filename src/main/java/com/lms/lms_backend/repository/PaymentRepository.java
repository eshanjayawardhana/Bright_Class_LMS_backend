package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Payment;
import com.lms.lms_backend.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByEnrollmentId(Long enrollmentId);
    long countByStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(p.enrollment.student.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.enrollment.student.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.enrollment.course.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Payment> searchPayments(@Param("search") String search, @Param("status") PaymentStatus status);
}
