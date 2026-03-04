package com.selfdev.platform.service;

import com.selfdev.platform.api.dto.HabitDtos;
import com.selfdev.platform.domain.Habit;
import com.selfdev.platform.domain.HabitCompletion;
import com.selfdev.platform.repo.HabitCompletionRepository;
import com.selfdev.platform.repo.HabitRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitService {
    private final HabitRepository habits;
    private final HabitCompletionRepository completions;

    public HabitService(HabitRepository habits, HabitCompletionRepository completions) {
        this.habits = habits;
        this.completions = completions;
    }

    @Transactional
    @CacheEvict(cacheNames = "todayHabits", key = "#userId")
    public HabitDtos.HabitResponse create(Long userId, HabitDtos.HabitCreateRequest req) {
        Habit habit = new Habit(userId, req.title(), req.description());
        habits.save(habit);
        return toHabitResponse(habit);
    }

    @Transactional(readOnly = true)
    public List<HabitDtos.HabitResponse> list(Long userId) {
        return habits.findAllByUserIdAndActiveTrueOrderByCreatedAtDesc(userId)
                .stream().map(this::toHabitResponse).toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "todayHabits", key = "#userId")
    public List<HabitDtos.HabitResponse> listToday(Long userId) {
        return list(userId);
    }

    @Transactional
    @CacheEvict(cacheNames = "todayHabits", key = "#userId")
    public HabitDtos.HabitResponse update(Long userId, Long habitId, HabitDtos.HabitUpdateRequest req) {
        Habit habit = habits.findByIdAndUserId(habitId, userId).orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        habit.setTitle(req.title());
        habit.setDescription(req.description());
        habit.setActive(req.active());
        return toHabitResponse(habit);
    }

    @Transactional
    public HabitDtos.CompletionResponse completeToday(Long userId, Long habitId, HabitDtos.CompleteRequest req) {
        LocalDate today = LocalDate.now();
        var existing = completions.findByUserIdAndHabitIdAndCompletionDate(userId, habitId, today);
        HabitCompletion saved;
        if (existing.isPresent()) {
            saved = existing.get();
            saved.setNote(req.note());
        } else {
            habits.findByIdAndUserId(habitId, userId).orElseThrow(() -> new IllegalArgumentException("Habit not found"));
            saved = new HabitCompletion(userId, habitId, today, req.note());
        }
        completions.save(saved);
        return new HabitDtos.CompletionResponse(saved.getId(), saved.getHabitId(), saved.getCompletionDate(), saved.getNote());
    }

    @Transactional(readOnly = true)
    public List<HabitDtos.CompletionResponse> history(Long userId) {
        return completions.findAllByUserIdOrderByCompletionDateDesc(userId)
                .stream().map(c -> new HabitDtos.CompletionResponse(c.getId(), c.getHabitId(), c.getCompletionDate(), c.getNote()))
                .toList();
    }

    private HabitDtos.HabitResponse toHabitResponse(Habit h) {
        return new HabitDtos.HabitResponse(h.getId(), h.getTitle(), h.getDescription(), h.isActive());
    }
}
