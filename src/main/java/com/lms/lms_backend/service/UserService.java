package com.lms.lms_backend.service;

import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.LoginResponse;
import com.lms.lms_backend.dto.RegisterRequest;
import com.lms.lms_backend.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
