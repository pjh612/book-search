package com.example.trevaribooksearch.infrastructure.persistence.jpa.entity;

import com.example.trevaribooksearch.infrastructure.persistence.hibernate.annotations.UuidV7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "authors")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuthorEntity extends BaseEntity {

    @Id
    @UuidV7Generator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public AuthorEntity(UUID id, String name, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.name = name;
    }
}
