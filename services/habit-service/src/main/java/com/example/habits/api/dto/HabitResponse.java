package com.example.habits.api.dto;

import com.example.habits.domain.habit.Habit;

import java.time.Instant;
import java.util.UUID;

public record HabitResponse(
        UUID id,
        String name,
        Habit.Frequency frequency,
        Instant createdAt,
        Instant updatedAt
) {
    public static HabitResponse from(Habit h) {
        return new HabitResponse(h.getId(), h.getName(), h.getFrequency(),
                h.getCreatedAt(), h.getUpdatedAt());
    }
}
