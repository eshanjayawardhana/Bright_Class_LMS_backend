package com.lms.lms_backend.dto;

import com.lms.lms_backend.entity.enums.ContentType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public class CourseContentResponseDTO {
    private Long id;
    private String title;
    private String description;
    private ContentType contentType;
    private String url;
    private List<ContentAttachmentDTO> attachments;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
    private Long courseId;
    private String lecturerName;

    public CourseContentResponseDTO() {
    }

    public CourseContentResponseDTO(Long id, String title, String description, ContentType contentType, String url, List<ContentAttachmentDTO> attachments, LocalDateTime scheduledTime, LocalDateTime createdAt, Long courseId, String lecturerName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.url = url;
        this.attachments = attachments;
        this.scheduledTime = scheduledTime;
        this.createdAt = createdAt;
        this.courseId = courseId;
        this.lecturerName = lecturerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ContentAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ContentAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }
}