package com.lms.lms_backend.repository;

import com.lms.lms_backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


public interface CourseRepository extends JpaRepository<Course, Long> {
    long countByLecturerId(Long lecturerId);
}

