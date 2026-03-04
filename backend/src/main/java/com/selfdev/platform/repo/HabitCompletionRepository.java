package com.selfdev.platform.repo;

import com.selfdev.platform.domain.HabitCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitCompletionRepository extends JpaRepository<HabitCompletion, Long> {
    List<HabitCompletion> findAllByUserIdAndCompletionDate(Long userId, LocalDate completionDate);
    List<HabitCompletion> findAllByUserIdOrderByCompletionDateDesc(Long userId);
    Optional<HabitCompletion> findByUserIdAndHabitIdAndCompletionDate(Long userId, Long habitId, LocalDate completionDate);
}
