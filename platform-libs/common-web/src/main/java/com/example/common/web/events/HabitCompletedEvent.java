package com.example.common.web.events;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain event emitted when a habit is completed for the first time on a given date.
 * Idempotent re-completions do NOT emit this event.
 */
public record HabitCompletedEvent(
        UUID habitId,
        UUID userId,
        LocalDate completedOn,
        Instant emittedAt
) {
    public HabitCompletedEvent(UUID habitId, UUID userId, LocalDate completedOn) {
        this(habitId, userId, completedOn, Instant.now());
    }
}
