package com.example.habits.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("select u from UserEntity u where lower(u.email) = lower(:email)")
    Optional<UserEntity> findByEmailIgnoreCase(@Param("email") String email);

    @Query("select count(u) > 0 from UserEntity u where lower(u.email) = lower(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
}
