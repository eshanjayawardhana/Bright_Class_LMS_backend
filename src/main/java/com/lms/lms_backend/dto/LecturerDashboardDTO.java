package com.lms.lms_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LecturerDashboardDTO {
    private long myCoursesCount;
    private long totalUploadedContent;
}
