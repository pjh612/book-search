package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {
}
