package com.lms.lms_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;
}
