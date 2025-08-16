package com.example.bookapi.infrastructure.persistence.jpa.repository;

import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherJpaRepository extends JpaRepository<PublisherEntity, UUID> {
}
