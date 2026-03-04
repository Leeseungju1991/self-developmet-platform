package com.selfdev.platform.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record SignUpRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 6, max = 60) String password,
            @NotBlank @Size(min = 2, max = 40) String displayName
    ) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record RefreshRequest(@NotBlank String refreshToken) {}

    public record AuthResponse(String accessToken, String refreshToken, UserMe me) {}

    public record UserMe(Long id, String email, String displayName) {}
}
