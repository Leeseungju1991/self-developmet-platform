package com.selfdev.platform.repo;

import com.selfdev.platform.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUserIdAndActiveTrueOrderByCreatedAtDesc(Long userId);
    Optional<Habit> findByIdAndUserId(Long id, Long userId);
}
