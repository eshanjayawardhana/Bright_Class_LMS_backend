package com.lms.lms_backend.mapper;

import com.lms.lms_backend.dto.EnrollmentRequest;
import com.lms.lms_backend.dto.EnrollmentResponse;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.Enrollment;
import com.lms.lms_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    // 1. Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "studentEmail")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "status", constant = "PENDING") // Initially always PENDING
    @Mapping(target = "enrollmentDate", expression = "java(java.time.LocalDateTime.now())")
    Enrollment toEntity(EnrollmentRequest request, String studentEmail, User student, Course course);

    // 2. Entity -> ResponseDTO
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    EnrollmentResponse toDto(Enrollment enrollment);
}
