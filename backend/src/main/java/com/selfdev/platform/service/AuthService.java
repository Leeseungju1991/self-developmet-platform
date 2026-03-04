package com.selfdev.platform.service;

import com.selfdev.platform.api.dto.AuthDtos;
import com.selfdev.platform.domain.RefreshToken;
import com.selfdev.platform.domain.User;
import com.selfdev.platform.repo.RefreshTokenRepository;
import com.selfdev.platform.repo.UserRepository;
import com.selfdev.platform.security.JwtService;
import com.selfdev.platform.security.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final PasswordHasher hasher;
    private final JwtService jwt;

    public AuthService(UserRepository users, RefreshTokenRepository refreshTokens, PasswordHasher hasher, JwtService jwt) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.hasher = hasher;
        this.jwt = jwt;
    }

    @Transactional
    public AuthDtos.AuthResponse signUp(AuthDtos.SignUpRequest req) {
        users.findByEmail(req.email()).ifPresent(u -> { throw new IllegalArgumentException("Email already exists"); });
        User user = new User(req.email(), hasher.hash(req.password()), req.displayName());
        users.save(user);
        return issueTokens(user);
    }

    @Transactional
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        User user = users.findByEmail(req.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!hasher.matches(req.password(), user.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");
        return issueTokens(user);
    }

    @Transactional
    public AuthDtos.AuthResponse refresh(AuthDtos.RefreshRequest req) {
        var parsed = jwt.parse(req.refreshToken()).getBody();
        if (!"refresh".equals(parsed.get("type", String.class))) throw new IllegalArgumentException("Invalid token type");

        String tokenHash = sha256(req.refreshToken());
        RefreshToken stored = refreshTokens.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) throw new IllegalArgumentException("Refresh token expired/revoked");

        Long userId = Long.parseLong(parsed.getSubject());
        User user = users.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        stored.revoke();
        refreshTokens.save(stored);

        return issueTokens(user);
    }

    private AuthDtos.AuthResponse issueTokens(User user) {
        String access = jwt.createAccessToken(user.getId(), user.getEmail());
        String refresh = jwt.createRefreshToken(user.getId(), user.getEmail());
        Instant refreshExp = Instant.now().plus(14, ChronoUnit.DAYS);
        refreshTokens.save(new RefreshToken(user.getId(), sha256(refresh), refreshExp));
        return new AuthDtos.AuthResponse(access, refresh, new AuthDtos.UserMe(user.getId(), user.getEmail(), user.getDisplayName()));
    }

    private static String sha256(String v) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(v.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
