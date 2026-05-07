package com.lms.lms_backend.mapper;

import com.lms.lms_backend.dto.CourseRequestDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import com.lms.lms_backend.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Course toEntity(CourseRequestDTO requestDTO);

    @Mapping(target = "lecturerEmail", source = "lecturer.email")
    CourseResponseDTO toDto(Course course);
}
