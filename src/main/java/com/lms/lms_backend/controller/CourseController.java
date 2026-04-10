package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.ApiResponse;
import com.lms.lms_backend.dto.CourseRequestDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // 🔐 ADMIN ONLY
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO response = courseService.createCourse(courseRequestDTO);

        return new ResponseEntity<>(
                ApiResponse.success("Course created successfully", response, HttpStatus.CREATED.value()), // 201
                HttpStatus.CREATED
        );
    }

    // 🔐 ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO request) {

        CourseResponseDTO response = courseService.updateCourse(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Course updated successfully", response, 200)
        );
    }

    // 🔐 ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {

        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully!");

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {

        return ResponseEntity.ok(
                ApiResponse.success("Successfully loaded all courses", courseService.getAllCourses(), 200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourse(@PathVariable Long id){

        return ResponseEntity.ok(
                ApiResponse.success("Successfully loaded course", courseService.getCourseById(id), 200)
        );
    }

}

