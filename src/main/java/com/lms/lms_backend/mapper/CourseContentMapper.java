package com.lms.lms_backend.mapper;

import com.lms.lms_backend.dto.CourseContentRequestDTO;
import com.lms.lms_backend.dto.CourseContentResponseDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.CourseContent;
import com.lms.lms_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseContentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "lecturer", source = "lecturer")
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "url", source = "request.url")
    @Mapping(target = "contentType", source = "request.contentType")
    @Mapping(target = "scheduledTime", source = "request.scheduledTime")
    CourseContent toEntity(CourseContentRequestDTO request, Course course, User lecturer);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "lecturerName", source = "lecturer.fullName")
    CourseContentResponseDTO toDto(CourseContent content);
}