package com.selfdev.platform.jobs;

import com.selfdev.platform.repo.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenCleanupJob {
    private final RefreshTokenRepository refreshTokens;
    public TokenCleanupJob(RefreshTokenRepository refreshTokens) { this.refreshTokens = refreshTokens; }

    @Scheduled(cron = "0 10 3 * * *")
    public void cleanupExpiredRefreshTokens() {
        var expired = refreshTokens.findAllByExpiresAtBefore(Instant.now());
        if (!expired.isEmpty()) refreshTokens.deleteAll(expired);
    }
}
