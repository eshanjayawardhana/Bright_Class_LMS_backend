package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.CourseRequestDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import com.lms.lms_backend.entity.Course;

import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO request);
    List<CourseResponseDTO> getAllCourses(String search);
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO request);
    void deleteCourse(Long id);
}
