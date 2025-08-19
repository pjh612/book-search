package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import com.example.bookapi.infrastructure.persistence.jpa.entity.UserEntity;
import com.example.bookapi.infrastructure.persistence.jpa.mapper.UserMapper;
import com.example.bookapi.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        UserEntity save = userJpaRepository.save(UserMapper.toEntity(user));

        return UserMapper.toDomain(save);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(UserMapper::toDomain);

    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}
