package com.lms.lms_backend.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
