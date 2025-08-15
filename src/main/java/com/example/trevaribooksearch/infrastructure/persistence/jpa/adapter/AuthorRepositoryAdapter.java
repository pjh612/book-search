package com.example.trevaribooksearch.infrastructure.persistence.jpa.adapter;

import com.example.trevaribooksearch.domain.model.Author;
import com.example.trevaribooksearch.domain.repository.AuthorRepository;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.AuthorEntity;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper.AuthorMapper;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.repository.AuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
