package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.CreateUserRequestDTO;
import com.lms.lms_backend.dto.LoginRequestDTO;
import com.lms.lms_backend.dto.LoginResponseDTO;
import com.lms.lms_backend.dto.RegisterRequestDTO;
import com.lms.lms_backend.entity.User;

public interface UserService {
    User register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
    User createLecture(CreateUserRequestDTO request);
    User createAdmin(CreateUserRequestDTO request);
}
