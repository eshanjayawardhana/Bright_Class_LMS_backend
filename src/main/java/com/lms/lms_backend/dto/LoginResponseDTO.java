package com.lms.lms_backend.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class LoginResponseDTO {

    private String email;
    private String message;
    private String token;
    private String fullName;
    private String role;

    public LoginResponseDTO(String email, String message, String token, String fullName, String role) {
        this.email = email;
        this.message = message;
        this.token = token;
        this.fullName = fullName;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
