package com.lms.lms_backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface SupabaseStorageService {

    String uploadFile(MultipartFile file) throws Exception;
}
