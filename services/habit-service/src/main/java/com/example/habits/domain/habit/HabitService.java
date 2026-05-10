package com.example.habits.domain.habit;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HabitService {

    private final HabitRepository repository;

    public HabitService(HabitRepository repository) {
        this.repository = repository;
    }

    public Habit create(UUID ownerId, String name, Habit.Frequency frequency) {
        return repository.save(Habit.create(ownerId, name, frequency));
    }

    public List<Habit> listOwned(UUID ownerId) {
        return repository.findAllByOwnerId(ownerId);
    }

    /**
     * Authorization-aware lookup. If the habit doesn't exist OR belongs to
     * someone else, throws HabitNotFoundException — indistinguishable, so
     * we don't leak existence to attackers.
     */
    public Habit getOwned(UUID habitId, UUID ownerId) {
        return repository.findById(habitId)
                .filter(h -> !h.isDeleted())
                .filter(h -> h.getOwnerId().equals(ownerId))
                .orElseThrow(() -> new HabitNotFoundException(habitId));
    }

    public Habit rename(UUID habitId, UUID ownerId, String newName) {
        Habit habit = getOwned(habitId, ownerId);
        habit.rename(newName);
        return repository.save(habit);
    }

    public void delete(UUID habitId, UUID ownerId) {
        Habit habit = getOwned(habitId, ownerId);
        habit.softDelete();
        repository.save(habit);
    }
}
