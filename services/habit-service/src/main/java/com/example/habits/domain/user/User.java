package com.example.habits.domain.user;

import java.time.Instant;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final String displayName;
    private final Instant createdAt;

    public User(UUID id, String email, String passwordHash, String displayName, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    public static User create(String email, String passwordHash, String displayName) {
        return new User(UUID.randomUUID(), email.toLowerCase(), passwordHash, displayName, Instant.now());
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getDisplayName() { return displayName; }
    public Instant getCreatedAt() { return createdAt; }
}
