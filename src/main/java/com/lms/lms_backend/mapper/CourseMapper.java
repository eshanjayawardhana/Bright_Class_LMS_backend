package com.lms.lms_backend.mapper;

import com.lms.lms_backend.dto.CourseRequestDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import com.lms.lms_backend.entity.Course;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseRequestDTO requestDTO);
    CourseResponseDTO toDto(Course course);
}
