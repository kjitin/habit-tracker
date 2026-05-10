package com.example.habits.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface SpringDataHabitRepository extends JpaRepository<HabitEntity, UUID> {
    List<HabitEntity> findAllByOwnerId(UUID ownerId);
}
