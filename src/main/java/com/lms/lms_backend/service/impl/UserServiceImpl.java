package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.dto.CreateUserRequestDTO;
import com.lms.lms_backend.dto.LoginRequestDTO;
import com.lms.lms_backend.dto.LoginResponseDTO;
import com.lms.lms_backend.dto.RegisterRequestDTO;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.Role;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.security.JwtUtil;
import com.lms.lms_backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Student
    @Override
    public User register(RegisterRequestDTO request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);

    }

    // Lecture
    @Override
    public User createLecture(CreateUserRequestDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.LECTURER)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    //Admin
    @Override
    public User createAdmin(CreateUserRequestDTO request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());

        return new LoginResponseDTO(
                user.getEmail(),
                "Login successful",
                token
        );
    }
}
