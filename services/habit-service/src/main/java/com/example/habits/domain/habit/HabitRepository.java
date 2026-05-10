package com.example.habits.domain.habit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain port. Implementations live in infrastructure/persistence
 * (JPA-backed) or as in-memory test doubles.
 */
public interface HabitRepository {
    Habit save(Habit habit);
    Optional<Habit> findById(UUID id);
    List<Habit> findAllByOwnerId(UUID ownerId);
    void delete(UUID id);
}
