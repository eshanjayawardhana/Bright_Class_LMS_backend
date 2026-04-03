package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.*;
import com.lms.lms_backend.entity.User;

public interface UserService {
    //student
    UserResponseDTO register(RegisterRequestDTO request);
    LoginResponseDTO login(LoginRequestDTO request);
    UserResponseDTO createLecture(CreateUserRequestDTO request);
    UserResponseDTO createAdmin(CreateUserRequestDTO request);
}
