package com.lms.lms_backend.dto;

import com.lms.lms_backend.entity.enums.ContentType;

import java.time.LocalDateTime;

public class CourseContentRequestDTO {
    private String title;
    private String description;
    private ContentType contentType;
    private String url;
    private LocalDateTime scheduledTime; // if Live class
    private Long courseId;

    public CourseContentRequestDTO() {
    }

    public CourseContentRequestDTO(String title, String description, ContentType contentType, String url, LocalDateTime scheduledTime, Long courseId) {
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.url = url;
        this.scheduledTime = scheduledTime;
        this.courseId = courseId;
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

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
