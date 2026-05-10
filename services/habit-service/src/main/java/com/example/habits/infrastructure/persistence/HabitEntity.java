package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.habit.Habit;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "habits")
class HabitEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Habit.Frequency frequency;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected HabitEntity() {}

    HabitEntity(UUID id, UUID ownerId, String name, Habit.Frequency frequency,
                boolean deleted, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.frequency = frequency;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static HabitEntity fromDomain(Habit h) {
        return new HabitEntity(h.getId(), h.getOwnerId(), h.getName(), h.getFrequency(),
                h.isDeleted(), h.getCreatedAt(), h.getUpdatedAt());
    }

    Habit toDomain() {
        return new Habit(id, ownerId, name, frequency, deleted, createdAt, updatedAt);
    }

    UUID getId() { return id; }
    UUID getOwnerId() { return ownerId; }
}
