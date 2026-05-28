package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.CourseContentRequestDTO;
import com.lms.lms_backend.dto.CourseContentResponseDTO;
import com.lms.lms_backend.entity.ContentAttachment;
import com.lms.lms_backend.entity.Course;
import com.lms.lms_backend.entity.CourseContent;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.ContentType;
import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import com.lms.lms_backend.exception.InvalidOperationException;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.mapper.CourseContentMapper;
import com.lms.lms_backend.repository.CourseContentRepository;
import com.lms.lms_backend.repository.CourseRepository;
import com.lms.lms_backend.repository.EnrollmentRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.CourseContentService;
import com.lms.lms_backend.service.SupabaseStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseContentServiceImpl implements CourseContentService {

    private final CourseContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseContentMapper contentMapper;
    private final SupabaseStorageService supabaseStorageService;

    public CourseContentServiceImpl(CourseContentRepository contentRepository, CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository, CourseContentMapper contentMapper, SupabaseStorageService supabaseStorageService) {
        this.contentRepository = contentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.contentMapper = contentMapper;
        this.supabaseStorageService = supabaseStorageService;
    }

//    @Override
//    public CourseContentResponseDTO createContent(CourseContentRequestDTO request, String lecturerEmail) {
//
//        User lecturer = userRepository.findByEmail(lecturerEmail)
//                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));
//
//        Course course = courseRepository.findById(request.getCourseId())
//                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
//
//        CourseContent content = contentMapper.toEntity(request, course, lecturer);
//        CourseContent savedContent = contentRepository.save(content);
//
//        return contentMapper.toDto(savedContent);
//    }

    @Override
    public CourseContentResponseDTO createContentWithFile(CourseContentRequestDTO request, java.util.List<org.springframework.web.multipart.MultipartFile> files, String name) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User lecturer = userRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        CourseContent content = new CourseContent();
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContentType(request.getContentType());
        content.setScheduledTime(request.getScheduledTime());
        content.setCourse(course);
        content.setLecturer(lecturer);
        content.setUrl(request.getUrl());

        // Process multiple files
        if (files != null && !files.isEmpty()) {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String fileUrl = supabaseStorageService.uploadFile(file);

                        ContentAttachment attachment = new ContentAttachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileUrl(fileUrl);
                        attachment.setCourseContent(content); // Link to the parent

                        content.getAttachments().add(attachment); // Add to the list
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to upload file to Supabase: " + e.getMessage());
                    }
                }
            }
        }

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

    @Override
    public CourseContentResponseDTO getContentById(Long contentId) {
        CourseContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course content not found"));
        return contentMapper.toDto(content);
    }

    @Override
    public List<CourseContentResponseDTO> getContentForLecturerOrAdmin(Long courseId) {
        return contentRepository.findByCourseId(courseId)
                .stream()
                .map(contentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseContentResponseDTO updateContentWithFiles(Long contentId, CourseContentRequestDTO request, java.util.List<org.springframework.web.multipart.MultipartFile> files, java.util.List<Long> deletedAttachmentIds, String name) {

        CourseContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course content not found"));

        User lecturer = userRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        // Only the owner or an admin should be able to edit
        if (!content.getLecturer().getId().equals(lecturer.getId()) && !lecturer.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to edit this content");
        }

        // 1. Update basic details
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setUrl(request.getUrl());
        content.setScheduledTime(request.getScheduledTime());

        // 2. Remove deleted attachments
        if (deletedAttachmentIds != null && !deletedAttachmentIds.isEmpty()) {
            content.getAttachments().removeIf(attachment -> deletedAttachmentIds.contains(attachment.getId()));
        }

        // 3. Process new files
        if (files != null && !files.isEmpty()) {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String fileUrl = supabaseStorageService.uploadFile(file);

                        ContentAttachment attachment = new ContentAttachment();
                        attachment.setFileName(file.getOriginalFilename());
                        attachment.setFileUrl(fileUrl);
                        attachment.setCourseContent(content);

                        content.getAttachments().add(attachment);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to upload new file to Supabase: " + e.getMessage());
                    }
                }
            }
        }

        CourseContent updatedContent = contentRepository.save(content);
        return contentMapper.toDto(updatedContent);
    }

    @Override
    public void deleteContent(Long id) {
        CourseContent content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course content not found with id: " + id));
        contentRepository.delete(content);
    }

}
