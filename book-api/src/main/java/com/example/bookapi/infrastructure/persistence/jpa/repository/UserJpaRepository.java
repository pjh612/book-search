package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
