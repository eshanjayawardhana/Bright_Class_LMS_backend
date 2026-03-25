package com.lms.lms_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class test {
    @GetMapping("/test")
    public String test() {
        return "Protected API is working!";
    }
    // this is made for check to API protection
}
