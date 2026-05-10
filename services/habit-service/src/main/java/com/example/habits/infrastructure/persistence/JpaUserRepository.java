package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.user.User;
import com.example.habits.domain.user.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("!dev-local")
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository delegate;

    public JpaUserRepository(SpringDataUserRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public User save(User user) {
        return delegate.save(UserEntity.fromDomain(user)).toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return delegate.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmailIgnoreCase(String email) {
        return delegate.findByEmailIgnoreCase(email).map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return delegate.existsByEmailIgnoreCase(email);
    }
}
