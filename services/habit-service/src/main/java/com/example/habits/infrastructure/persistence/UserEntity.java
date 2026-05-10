package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserEntity() {}

    UserEntity(UUID id, String email, String passwordHash, String displayName, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    static UserEntity fromDomain(User u) {
        return new UserEntity(u.getId(), u.getEmail(), u.getPasswordHash(),
                u.getDisplayName(), u.getCreatedAt());
    }

    User toDomain() {
        return new User(id, email, passwordHash, displayName, createdAt);
    }
}
