package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.LoginRequest;
import com.lms.lms_backend.dto.LoginResponse;
import com.lms.lms_backend.dto.RegisterRequest;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }


    // this is made for check to API protection
    @GetMapping("/test")
    public String test() {
        return "Protected API is working!";
    }



}
