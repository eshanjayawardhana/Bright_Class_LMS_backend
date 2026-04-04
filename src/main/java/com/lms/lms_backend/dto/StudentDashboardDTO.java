package com.lms.lms_backend.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class StudentDashboardDTO {
    private long approvedCoursesCount;
    private long pendingCoursesCount;
    private long rejectedCoursesCount;
    private List<EnrollmentResponseDTO> myEnrollments;
}
