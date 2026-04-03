package com.lms.lms_backend.controller;

import com.lms.lms_backend.dto.CreateUserRequestDTO;
import com.lms.lms_backend.dto.UserResponseDTO;
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
    public ResponseEntity<UserResponseDTO> createLecture(@Valid @RequestBody CreateUserRequestDTO request){
        return new ResponseEntity<>(userService.createLecture(request), HttpStatus.CREATED);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody CreateUserRequestDTO request){
        return new ResponseEntity<>(userService.createAdmin(request), HttpStatus.CREATED);
    }
}
