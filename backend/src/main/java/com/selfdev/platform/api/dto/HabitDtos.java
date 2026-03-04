package com.selfdev.platform.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class HabitDtos {
    public record HabitCreateRequest(
            @NotBlank @Size(min=1, max=120) String title,
            @Size(max=300) String description
    ) {}

    public record HabitUpdateRequest(
            @NotBlank @Size(min=1, max=120) String title,
            @Size(max=300) String description,
            boolean active
    ) {}

    public record HabitResponse(Long id, String title, String description, boolean active) {}

    public record CompleteRequest(String note) {}

    public record CompletionResponse(Long id, Long habitId, LocalDate date, String note) {}
}
