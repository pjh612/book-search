package com.example.bookapi.infrastructure.persistence.jpa.mapper;

import com.example.bookapi.domain.model.User;
import com.example.bookapi.infrastructure.persistence.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UserMapperTest {

    @Test
    void toEntity() {
        // Given
        User user = User.create(
                "testUser",
                "password",
                "ROLE_USER"
        );

        // When
        UserEntity entity = UserMapper.toEntity(user);

        // Then
        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getUsername(), entity.getUsername());
        assertEquals(user.getPassword(), entity.getPassword());
        assertEquals(user.getRole(), entity.getRole());
        assertNotNull(entity.getCreatedAt());
        assertEquals(entity.getCreatedBy(), user.getUsername());
    }

    @Test
    void toDomain() {
        // Given
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(
                id,
                "testUser",
                "password",
                "ROLE_USER",
                Instant.now(),
                Instant.now(),
                id.toString(),
                id.toString()
        );

        // When
        User user = UserMapper.toDomain(entity);

        // Then
        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getUsername(), user.getUsername());
        assertEquals(entity.getPassword(), user.getPassword());
        assertEquals(entity.getRole(), user.getRole());
        assertThat(entity.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        assertThat(entity.getCreatedBy()).isEqualTo(entity.getCreatedBy());
        assertThat(entity.getUpdatedBy()).isEqualTo(entity.getUpdatedBy());
    }
}