package com.example.habits.domain.habit;

import java.time.Instant;
import java.util.UUID;

/**
 * Pure domain model for a Habit. Independent of JPA, JSON, or anything else.
 * Invariants live here — name length, frequency required.
 */
public class Habit {

    public enum Frequency { DAILY, WEEKLY }

    private final UUID id;
    private final UUID ownerId;
    private String name;
    private Frequency frequency;
    private boolean deleted;
    private final Instant createdAt;
    private Instant updatedAt;

    public Habit(UUID id, UUID ownerId, String name, Frequency frequency,
                 boolean deleted, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        setName(name);
        setFrequency(frequency);
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Habit create(UUID ownerId, String name, Frequency frequency) {
        Instant now = Instant.now();
        return new Habit(UUID.randomUUID(), ownerId, name, frequency, false, now, now);
    }

    public void rename(String name) {
        setName(name);
        this.updatedAt = Instant.now();
    }

    public void setFrequency(Frequency frequency) {
        if (frequency == null) {
            throw new IllegalArgumentException("frequency is required");
        }
        this.frequency = frequency;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 200) {
            throw new IllegalArgumentException("name too long");
        }
        this.name = name;
    }

    public void softDelete() {
        this.deleted = true;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public Frequency getFrequency() { return frequency; }
    public boolean isDeleted() { return deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
