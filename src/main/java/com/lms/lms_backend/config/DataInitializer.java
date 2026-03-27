package com.lms.lms_backend.config;

import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.entity.enums.Role;
import com.lms.lms_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // set application.yaml values
    @Value("${app.admin.default.email}")
    private String adminEmail;

    @Value("${app.admin.default.password}")
    private String adminPassword;

    @Override
    public void run(String... args){

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Default Admin User Created Successfully with config values!");
        }
    }
}
