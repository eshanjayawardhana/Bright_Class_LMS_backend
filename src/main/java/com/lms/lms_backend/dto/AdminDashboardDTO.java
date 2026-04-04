package com.lms.lms_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardDTO {
    private long totalUsers;
    private long totalCourses;

    private long totalEnrollments;
    private long pendingEnrollments;
    private long rejectEnrollments;

    private long totalPayments;
    private long pendingPayments;
    private long rejectPayments;
}