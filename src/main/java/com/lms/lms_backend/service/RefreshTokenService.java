package com.lms.lms_backend.service;

import com.lms.lms_backend.entity.RefreshToken;
public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
}
