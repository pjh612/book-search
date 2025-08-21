package com.example.bookapi.infrastructure.persistence.jpa.entity;

import com.example.bookapi.infrastructure.persistence.hibernate.annotations.UuidV7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Id
    @UuidV7Generator
    private UUID id;

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    public UserEntity(UUID id, String username, String password, String role, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
