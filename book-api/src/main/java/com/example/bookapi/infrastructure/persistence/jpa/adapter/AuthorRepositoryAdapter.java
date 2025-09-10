package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.domain.model.Author;
import com.example.bookapi.domain.repository.AuthorRepository;
import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.bookapi.infrastructure.persistence.jpa.mapper.AuthorMapper;
import com.example.bookapi.infrastructure.persistence.jpa.repository.AuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryAdapter implements AuthorRepository {
    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public Author save(Author author) {
        AuthorEntity save = authorJpaRepository.save(
                AuthorMapper.toEntity(author)
        );

        return AuthorMapper.toDomain(save);
    }

    @Override
    public Optional<Author> findById(UUID uuid) {
        return authorJpaRepository.findById(uuid)
                .map(AuthorMapper::toDomain);
    }
}
