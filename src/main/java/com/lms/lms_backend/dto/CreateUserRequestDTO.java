package com.lms.lms_backend.dto;

public class CreateUserRequestDTO {

    private String email;
    private String password;

    public CreateUserRequestDTO() {
    }

    public CreateUserRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
