package com.example.bookapi.infrastructure.persistence.jpa.mapper;

import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.Author;
import com.example.bookapi.infrastructure.persistence.jpa.entity.AuthorEntity;

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
                author.getAuditInfo() == null? null : author.getAuditInfo().getCreatedAt(),
                author.getAuditInfo() == null? null : author.getAuditInfo().getUpdatedAt(),
                author.getAuditInfo() == null? null : author.getAuditInfo().getCreatedBy(),
                author.getAuditInfo() == null? null : author.getAuditInfo().getUpdatedBy()
        );
    }
}