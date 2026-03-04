package com.selfdev.platform.api;

import com.selfdev.platform.api.dto.HabitDtos;
import com.selfdev.platform.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {
    private final HabitService habits;
    public HabitController(HabitService habits) { this.habits = habits; }

    private Long userId(Authentication auth) { return (Long) auth.getPrincipal(); }

    @GetMapping
    public ResponseEntity<List<HabitDtos.HabitResponse>> list(Authentication auth) {
        return ResponseEntity.ok(habits.list(userId(auth)));
    }

    @GetMapping("/today")
    public ResponseEntity<List<HabitDtos.HabitResponse>> today(Authentication auth) {
        return ResponseEntity.ok(habits.listToday(userId(auth)));
    }

    @PostMapping
    public ResponseEntity<HabitDtos.HabitResponse> create(Authentication auth, @Valid @RequestBody HabitDtos.HabitCreateRequest req) {
        return ResponseEntity.ok(habits.create(userId(auth), req));
    }

    @PutMapping("/{habitId}")
    public ResponseEntity<HabitDtos.HabitResponse> update(Authentication auth, @PathVariable Long habitId, @Valid @RequestBody HabitDtos.HabitUpdateRequest req) {
        return ResponseEntity.ok(habits.update(userId(auth), habitId, req));
    }

    @PostMapping("/{habitId}/complete")
    public ResponseEntity<HabitDtos.CompletionResponse> complete(Authentication auth, @PathVariable Long habitId, @RequestBody HabitDtos.CompleteRequest req) {
        return ResponseEntity.ok(habits.completeToday(userId(auth), habitId, req));
    }

    @GetMapping("/history")
    public ResponseEntity<List<HabitDtos.CompletionResponse>> history(Authentication auth) {
        return ResponseEntity.ok(habits.history(userId(auth)));
    }
}
