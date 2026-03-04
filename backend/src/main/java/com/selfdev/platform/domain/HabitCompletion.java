package com.selfdev.platform.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "habit_completions", indexes = {
        @Index(name = "idx_completion_user_date", columnList = "userId,completionDate"),
        @Index(name = "idx_completion_habit_date", columnList = "habitId,completionDate")
})
public class HabitCompletion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long habitId;

    @Column(nullable = false)
    private LocalDate completionDate;

    @Column(nullable = false, length = 500)
    private String note;

    protected HabitCompletion() {}

    public HabitCompletion(Long userId, Long habitId, LocalDate completionDate, String note) {
        this.userId = userId;
        this.habitId = habitId;
        this.completionDate = completionDate;
        this.note = note == null ? "" : note;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getHabitId() { return habitId; }
    public LocalDate getCompletionDate() { return completionDate; }
    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }
}
