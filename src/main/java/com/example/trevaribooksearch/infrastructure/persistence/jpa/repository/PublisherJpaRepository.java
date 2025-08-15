package com.example.trevaribooksearch.infrastructure.persistence.jpa.repository;

import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherJpaRepository extends JpaRepository<PublisherEntity, UUID> {
}
