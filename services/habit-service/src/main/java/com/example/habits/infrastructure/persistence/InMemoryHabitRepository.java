package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.habit.Habit;
import com.example.habits.domain.habit.HabitRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("dev-local")
public class InMemoryHabitRepository implements HabitRepository {

    private final Map<UUID, Habit> store = new ConcurrentHashMap<>();

    @Override
    public Habit save(Habit habit) {
        store.put(habit.getId(), habit);
        return habit;
    }

    @Override
    public Optional<Habit> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Habit> findAllByOwnerId(UUID ownerId) {
        return store.values().stream()
                .filter(h -> h.getOwnerId().equals(ownerId))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }
}
