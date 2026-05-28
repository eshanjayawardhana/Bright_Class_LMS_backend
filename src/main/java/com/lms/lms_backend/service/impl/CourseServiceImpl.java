package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.CourseRequestDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.CourseMapper;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO request) {

        User lecturer = userRepository.findByEmail(request.getLecturerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with email: " + request.getLecturerEmail()));
        // DTO -> Entity (using Builder pattern)
        Course course = Course.builder()
                .title(request.getTitle())
                .code(request.getCode())
                .description(request.getDescription())
                .year(request.getYear())
                .semester(request.getSemester())
                .category(request.getCategory())
                .lecturer(lecturer)
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToResponseDTO(savedCourse);
    }

    @Override
    public List<CourseResponseDTO> getAllCourses(String search) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Course> courses;

        if (search != null && !search.trim().isEmpty()) {
            courses = courseRepository.searchCourses(search);
        } else {
            courses = courseRepository.findAll();
        }


        if (!isAdmin) {
            courses = courses.stream()
                    .filter(c -> c.getLecturer() != null && c.getLecturer().getEmail().equals(currentUserEmail))
                    .collect(Collectors.toList());
        }
        return courses.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

//        return mapToResponseDTO(course);
        return courseMapper.toDto(course); // ---> using Course Mapper
    }

    @Override
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO request) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        User lecturer = userRepository.findByEmail(request.getLecturerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with email: " + request.getLecturerEmail()));

        existingCourse.setTitle(request.getTitle());
        existingCourse.setCode(request.getCode());
        existingCourse.setDescription(request.getDescription());
        existingCourse.setYear(request.getYear());
        existingCourse.setSemester(request.getSemester());
        existingCourse.setCategory(request.getCategory());
        existingCourse.setLecturer(lecturer);

        Course updatedCourse = courseRepository.save(existingCourse);
        return mapToResponseDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }



    // Entity -> Response DTO
    private CourseResponseDTO mapToResponseDTO(Course course) {
        return CourseResponseDTO.builder()
                .id(course.getId())
                .title(course.getTitle())
                .code(course.getCode())
                .description(course.getDescription())
                .year(course.getYear())
                .semester(course.getSemester())
                .category(course.getCategory())
                .lecturerEmail(course.getLecturer() != null ? course.getLecturer().getEmail() : null)
                .createdAt(course.getCreatedAt())
                .build();
    }
}
