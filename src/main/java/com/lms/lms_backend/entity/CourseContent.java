package com.lms.lms_backend.entity;

import com.lms.lms_backend.entity.enums.ContentType;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_contents")
@Builder
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Column(nullable = false)
    private String url;

    private LocalDateTime scheduledTime; // Live Class start time

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    private User lecturer;

    public CourseContent() {
    }

    public CourseContent(Long id, String title, String description, ContentType contentType, String url, LocalDateTime scheduledTime, LocalDateTime createdAt, Course course, User lecturer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.url = url;
        this.scheduledTime = scheduledTime;
        this.createdAt = createdAt;
        this.course = course;
        this.lecturer = lecturer;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getLecturer() {
        return lecturer;
    }

    public void setLecturer(User lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return "CourseContent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", contentType=" + contentType +
                ", url='" + url + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", createdAt=" + createdAt +
                ", course=" + course +
                ", lecturer=" + lecturer +
                '}';
    }
}
