package com.lms.lms_backend.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponse {

    private String email;
    private String message;
    private String token;

    public LoginResponse(String email, String message, String token) {
        this.email = email;
        this.message = message;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
