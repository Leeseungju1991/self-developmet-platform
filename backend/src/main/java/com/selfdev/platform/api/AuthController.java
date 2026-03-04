package com.selfdev.platform.api;

import com.selfdev.platform.api.dto.AuthDtos;
import com.selfdev.platform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/signup")
    public ResponseEntity<AuthDtos.AuthResponse> signUp(@Valid @RequestBody AuthDtos.SignUpRequest req) {
        return ResponseEntity.ok(auth.signUp(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthDtos.AuthResponse> refresh(@Valid @RequestBody AuthDtos.RefreshRequest req) {
        return ResponseEntity.ok(auth.refresh(req));
    }
}
