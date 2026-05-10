package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.habit.Habit;
import com.example.habits.domain.habit.HabitRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("!dev-local")
public class JpaHabitRepository implements HabitRepository {

    private final SpringDataHabitRepository delegate;

    public JpaHabitRepository(SpringDataHabitRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Habit save(Habit habit) {
        return delegate.save(HabitEntity.fromDomain(habit)).toDomain();
    }

    @Override
    public Optional<Habit> findById(UUID id) {
        return delegate.findById(id).map(HabitEntity::toDomain);
    }

    @Override
    public List<Habit> findAllByOwnerId(UUID ownerId) {
        return delegate.findAllByOwnerId(ownerId).stream()
                .map(HabitEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        delegate.deleteById(id);
    }
}
