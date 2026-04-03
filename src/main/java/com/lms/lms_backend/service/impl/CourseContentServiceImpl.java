package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.CourseContentRequestDTO;
import com.lms.lms_backend.dto.CourseContentResponseDTO;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.CourseContent;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.exception.InvalidOperationException;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.CourseContentMapper;
import com.lms.lms_backend.repository.CourseContentRepository;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.CourseContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseContentServiceImpl implements CourseContentService {

    private final CourseContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseContentMapper contentMapper;

    public CourseContentServiceImpl(CourseContentRepository contentRepository, CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository, CourseContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.contentMapper = contentMapper;
    }

    @Override
    public CourseContentResponseDTO createContent(CourseContentRequestDTO request, String lecturerEmail) {

        User lecturer = userRepository.findByEmail(lecturerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        CourseContent content = contentMapper.toEntity(request, course, lecturer);
        CourseContent savedContent = contentRepository.save(content);

        return contentMapper.toDto(savedContent);
    }

    @Override
    public List<CourseContentResponseDTO> getCourseContent(Long courseId, String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isApproved = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(),
                courseId,
                EnrollmentStatus.APPROVED
        );

        if (!isApproved) {
            throw new InvalidOperationException("Access denied: You are not an approved student for this course.");
        }

        return contentRepository.findByCourseId(courseId)
                .stream()
                .map(contentMapper::toDto)
                .collect(Collectors.toList());
    }

}
