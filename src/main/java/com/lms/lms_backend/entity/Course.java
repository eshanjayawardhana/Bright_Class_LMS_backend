package com.lms.lms_backend.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String year;      // e.g., Year 1
    private String semester;  // e.g., Semester 1
    private String category;  // optional (Degree / Skill)
//    private String lecturerEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private User lecturer;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, unique = true)
    private String code;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Course() {
    }

    public Course(Long id, String title, String description, String year, String semester, String category, User lecturer, LocalDateTime createdAt, String code) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.year = year;
        this.semester = semester;
        this.category = category;
        this.lecturer = lecturer;
        this.createdAt = createdAt;
        this.code = code;
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
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public User getLecturer() {
        return lecturer;
    }

    public void setLecturer(User lecturer) {
        this.lecturer = lecturer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", year='" + year + '\'' +
                ", semester='" + semester + '\'' +
                ", category='" + category + '\'' +
                ", lecturer=" + lecturer +
                ", createdAt=" + createdAt +
                ", code='" + code + '\'' +
                '}';
    }
}
