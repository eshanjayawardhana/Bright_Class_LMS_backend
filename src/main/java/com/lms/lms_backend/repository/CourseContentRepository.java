package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {

    List<CourseContent> findByCourseId(Long courseId);
    long countByLecturerId(Long lecturerId);
}
