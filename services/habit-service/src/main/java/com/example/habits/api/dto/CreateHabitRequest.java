package com.example.habits.api.dto;

import com.example.habits.domain.habit.Habit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHabitRequest(
        @NotBlank @Size(max = 200) String name,
        @NotNull Habit.Frequency frequency
) {}
