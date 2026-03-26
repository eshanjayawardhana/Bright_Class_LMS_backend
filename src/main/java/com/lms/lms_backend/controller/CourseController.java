package com.lms.lms_backend.controller;

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
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO response = courseService.createCourse(courseRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created status
    }

    // 🔐 ADMIN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    // 🔐 ADMIN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses()); // 200 OK status
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

}

