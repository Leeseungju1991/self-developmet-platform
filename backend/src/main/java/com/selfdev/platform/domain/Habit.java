package com.selfdev.platform.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "habits", indexes = {
        @Index(name = "idx_habits_user_id", columnList = "userId")
})
public class Habit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Habit() {}

    public Habit(Long userId, String title, String description) {
        this.userId = userId;
        this.title = title;
        this.description = description == null ? "" : description;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(boolean active) { this.active = active; }
}
