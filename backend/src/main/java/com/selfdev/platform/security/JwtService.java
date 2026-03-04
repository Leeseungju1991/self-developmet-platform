package com.selfdev.platform.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .setIssuer(props.issuer())
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(Map.of("email", email, "type", "access"))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(props.refreshTokenDays(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setIssuer(props.issuer())
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(Map.of("email", email, "type", "refresh"))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(props.issuer())
                .build()
                .parseClaimsJws(token);
    }
}
