package com.example.bookapi.infrastructure.persistence.jpa.entity;


import com.example.bookapi.infrastructure.persistence.hibernate.annotations.UuidV7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@AllArgsConstructor
@Table(name = "publishers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublisherEntity extends BaseEntity {

    @Id
    @UuidV7Generator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public PublisherEntity(UUID id, String name, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.name = name;
    }
}