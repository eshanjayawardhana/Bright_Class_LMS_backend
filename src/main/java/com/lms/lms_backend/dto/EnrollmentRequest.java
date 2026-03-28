package com.lms.lms_backend.dto;

public class EnrollmentRequest {

    private String fullName;
    private String nic;
    private String bitId;
    private String phone;
    private Long courseId;

    public EnrollmentRequest() {
    }

    public EnrollmentRequest(String fullName, String nic, String bitId, String phone, Long courseId) {
        this.fullName = fullName;
        this.nic = nic;
        this.bitId = bitId;
        this.phone = phone;
        this.courseId = courseId;
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
