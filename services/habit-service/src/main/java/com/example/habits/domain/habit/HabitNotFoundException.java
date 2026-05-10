package com.example.habits.domain.habit;

import java.util.UUID;

public class HabitNotFoundException extends RuntimeException {
    private final UUID habitId;

    public HabitNotFoundException(UUID habitId) {
        super("Habit not found: " + habitId);
        this.habitId = habitId;
    }

    public UUID getHabitId() { return habitId; }
}
