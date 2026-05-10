package com.example.habits.infrastructure.persistence;

import com.example.habits.domain.user.User;
import com.example.habits.domain.user.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("dev-local")
public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> store = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByEmailIgnoreCase(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return findByEmailIgnoreCase(email).isPresent();
    }
}
