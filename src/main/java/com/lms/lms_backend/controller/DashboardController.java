package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.AdminDashboardDTO;
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
    public ResponseEntity<AdminDashboardDTO> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    // LECTURER
    @GetMapping("/lecturer")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<LecturerDashboardDTO> getLecturerDashboard(Authentication auth) {
        return ResponseEntity.ok(dashboardService.getInstructorDashboard(auth.getName()));
    }

    // STUDENT
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDashboardDTO> getStudentDashboard(Authentication auth) {
        return ResponseEntity.ok(dashboardService.getStudentDashboard(auth.getName()));
    }
}
