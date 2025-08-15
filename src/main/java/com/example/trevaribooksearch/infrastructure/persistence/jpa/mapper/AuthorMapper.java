package com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper;

import com.example.trevaribooksearch.domain.model.AuditInfo;
import com.example.trevaribooksearch.domain.model.Author;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.AuthorEntity;

public class AuthorMapper {
    public static Author toDomain(AuthorEntity entity) {
        return new Author(
                entity.getId(),
                entity.getName(),
                new AuditInfo(
                        entity.getCreatedAt(),
                        entity.getCreatedBy(),
                        entity.getUpdatedAt(),
                        entity.getUpdatedBy()
                )
        );
    }

    public static AuthorEntity toEntity(Author author) {
        return new AuthorEntity(
                author.getId(),
                author.getName(),
                author.getAuditInfo().getCreatedAt(),
                author.getAuditInfo().getUpdatedAt(),
                author.getAuditInfo().getCreatedBy(),
                author.getAuditInfo().getUpdatedBy()
        );
    }
}