package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.LoginResponse;
import com.lms.lms_backend.dto.RegisterRequest;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.Role;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.security.JwtUtil;
import com.lms.lms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceIMPL(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(RegisterRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);

    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());

        return new LoginResponse(
                user.getEmail(),
                "Login successful",
                token
        );
    }

}
