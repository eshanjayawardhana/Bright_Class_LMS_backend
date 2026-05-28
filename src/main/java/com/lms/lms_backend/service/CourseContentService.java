package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.CourseContentRequestDTO;
import com.lms.lms_backend.dto.CourseContentResponseDTO;
import com.lms.lms_backend.dto.CourseResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseContentService {
    List<CourseContentResponseDTO> getCourseContent(Long courseId, String name);

    //    CourseContentResponseDTO createContent(CourseContentRequestDTO request, String name);
    List<CourseContentResponseDTO> getContentForLecturerOrAdmin(Long courseId);

    void deleteContent(Long id);

    CourseContentResponseDTO createContentWithFile(CourseContentRequestDTO request, java.util.List<org.springframework.web.multipart.MultipartFile> files, String name);

    CourseContentResponseDTO updateContentWithFiles(Long contentId, CourseContentRequestDTO request, java.util.List<org.springframework.web.multipart.MultipartFile> files, java.util.List<Long> deletedAttachmentIds, String name);

    CourseContentResponseDTO getContentById(Long contentId);
}