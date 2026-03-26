package com.lms.lms_backend.dto;

import lombok.Builder;

@Builder
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String year;
    private String semester;
    private String category;
    private String lecturerEmail;

    public CourseResponseDTO() {
    }

    public CourseResponseDTO(Long id, String title, String description, String year, String semester, String category, String lecturerEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.year = year;
        this.semester = semester;
        this.category = category;
        this.lecturerEmail = lecturerEmail;
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

    public String getLecturerEmail() {
        return lecturerEmail;
    }

    public void setLecturerEmail(String lecturerEmail) {
        this.lecturerEmail = lecturerEmail;
    }
}
