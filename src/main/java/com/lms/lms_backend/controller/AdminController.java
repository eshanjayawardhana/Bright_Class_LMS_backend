package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.CreateUserRequest;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-lecture")
    public ResponseEntity<User> createLecture(@Valid @RequestBody CreateUserRequest createUserRequest){
        User response = userService.createLecture(createUserRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created status
    }

    @PostMapping("/create-admin")
    public ResponseEntity<User> createAdmin(@Valid @RequestBody CreateUserRequest createUserRequest){
        User response = userService.createAdmin(createUserRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created status
    }
}
