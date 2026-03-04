package com.selfdev.platform.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public String hash(String raw) { return encoder.encode(raw); }
    public boolean matches(String raw, String hash) { return encoder.matches(raw, hash); }
}
