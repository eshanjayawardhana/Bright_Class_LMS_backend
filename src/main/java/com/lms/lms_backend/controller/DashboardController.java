package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.AdminDashboardDTO;
import com.lms.lms_backend.dto.ApiResponse;
import com.lms.lms_backend.dto.LecturerDashboardDTO;
import com.lms.lms_backend.dto.StudentDashboardDTO;
import com.lms.lms_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // ADMIN
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> getAdminDashboard() {
//        return ResponseEntity.ok(dashboardService.getAdminDashboard());

        return ResponseEntity.ok(
                ApiResponse.success("Admin Dashboard", dashboardService.getAdminDashboard(), 200)
        );
    }

    // LECTURER
    @GetMapping("/lecturer")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<ApiResponse<LecturerDashboardDTO>> getLecturerDashboard(Authentication auth) {
//        return ResponseEntity.ok(dashboardService.getInstructorDashboard(auth.getName()));

        return ResponseEntity.ok(
                ApiResponse.success("Lecture Dashboard", dashboardService.getInstructorDashboard(auth.getName()), 200)
        );
    }

    // STUDENT
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<StudentDashboardDTO>> getStudentDashboard(Authentication auth) {
//        return ResponseEntity.ok(dashboardService.getStudentDashboard(auth.getName()));

        return ResponseEntity.ok(
                ApiResponse.success("Student Dashboard", dashboardService.getStudentDashboard(auth.getName()), 200)
        );
    }
}
