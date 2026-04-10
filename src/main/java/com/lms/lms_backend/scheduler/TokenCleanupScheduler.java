package com.lms.lms_backend.scheduler;

import com.lms.lms_backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    // 🔥 "cron" run every 12.00 pm
    @Scheduled(cron = "0 0 0 * * ?") // set like this for testing @Scheduled(fixedRate = 10000)-> 10seconds
    @Transactional
    public void cleanExpiredRefreshTokens() {

        log.info("Starting expired refresh token cleanup process...");

        int deletedCount = refreshTokenRepository.deleteExpiredTokens(Instant.now());

        log.info("Finished expired refresh token cleanup. Deleted {} tokens.", deletedCount);
    }
}
