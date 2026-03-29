package com.lms.lms_backend.dto;

import com.lms.lms_backend.entity.enums.EnrollmentStatus;

import java.time.LocalDateTime;

public class EnrollmentResponseDTO {
    private Long id;
    private String fullName;
    private String nic;
    private String bitId;
    private String phone;
    private String email;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private Long courseId;
    private String courseTitle;

    public EnrollmentResponseDTO() {
    }

    public EnrollmentResponseDTO(Long id, String fullName, String nic, String bitId, String phone, String email, LocalDateTime enrollmentDate, EnrollmentStatus status, Long courseId, String courseTitle) {
        this.id = id;
        this.fullName = fullName;
        this.nic = nic;
        this.bitId = bitId;
        this.phone = phone;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getBitId() {
        return bitId;
    }

    public void setBitId(String bitId) {
        this.bitId = bitId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
