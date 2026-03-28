package com.lms.lms_backend.entity;

import com.lms.lms_backend.entity.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String nic;
    private String bitId;
    private String phone;
    private String email;

    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    // relations
    @ManyToOne
    private User student;

    @ManyToOne
    private Course course;

    public Enrollment() {
    }

    public Enrollment(Long id, String fullName, String nic, String bitId, String phone, String email, LocalDateTime enrollmentDate, EnrollmentStatus status, User student, Course course) {
        this.id = id;
        this.fullName = fullName;
        this.nic = nic;
        this.bitId = bitId;
        this.phone = phone;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.student = student;
        this.course = course;
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

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", nic='" + nic + '\'' +
                ", bitId='" + bitId + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", status=" + status +
                ", student=" + student +
                ", course=" + course +
                '}';
    }
}
