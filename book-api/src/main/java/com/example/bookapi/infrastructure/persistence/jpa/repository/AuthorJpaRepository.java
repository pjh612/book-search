package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, UUID> {
}
