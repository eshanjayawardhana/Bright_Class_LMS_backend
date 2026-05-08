package com.lms.lms_backend.mapper;

import com.lms.lms_backend.dto.PaymentRequestDTO;
import com.lms.lms_backend.dto.PaymentResponseDTO;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "paymentDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "enrollment", source = "enrollment")
    @Mapping(target = "slipUrl", ignore = true)
    Payment toEntity(PaymentRequestDTO request, Enrollment enrollment);

    // Entity -> ResponseDTO
    @Mapping(target = "enrollmentId", source = "enrollment.id")
    @Mapping(target = "studentName", source = "enrollment.fullName")
    @Mapping(target = "studentEmail", source = "enrollment.email")
    @Mapping(target = "courseTitle", source = "enrollment.course.title")
    PaymentResponseDTO toDto(Payment payment);
}
