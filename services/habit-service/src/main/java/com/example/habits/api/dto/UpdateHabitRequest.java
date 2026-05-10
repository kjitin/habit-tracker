package com.example.habits.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateHabitRequest(
        @NotBlank @Size(max = 200) String name
) {}
