package com.lms.lms_backend.service.impl;

import com.lms.lms_backend.entity.RefreshToken;
import com.lms.lms_backend.entity.User;
import com.lms.lms_backend.exception.ResourceNotFoundException;
import com.lms.lms_backend.repository.RefreshTokenRepository;
import com.lms.lms_backend.repository.UserRepository;
import com.lms.lms_backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // how long valid this Refresh token (7 days)
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs; // application.properties

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // one Active Refresh Token must be have one user, then delete old token
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token); // when expired the time then delete token form DB
            throw new RuntimeException("Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}
