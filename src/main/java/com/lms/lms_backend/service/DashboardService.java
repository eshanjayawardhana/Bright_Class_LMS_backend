package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.AdminDashboardDTO;
import com.lms.lms_backend.dto.LecturerDashboardDTO;
import com.lms.lms_backend.dto.StudentDashboardDTO;

public interface DashboardService {
    AdminDashboardDTO getAdminDashboard();
    StudentDashboardDTO getStudentDashboard(String email);
    LecturerDashboardDTO getInstructorDashboard(String email);
}
