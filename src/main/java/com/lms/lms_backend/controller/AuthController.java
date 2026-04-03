package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.LoginRequestDTO;
import com.lms.lms_backend.dto.LoginResponseDTO;
import com.lms.lms_backend.dto.RegisterRequestDTO;
import com.lms.lms_backend.dto.UserResponseDTO;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.service.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(userService.login(request));
    }


    // this is made for check to API protection
//    @GetMapping("/test")
//    public String test() {
//        return "Protected API is working!";
//    }

}
