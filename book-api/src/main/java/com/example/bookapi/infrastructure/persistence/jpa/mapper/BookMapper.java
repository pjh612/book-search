package com.example.bookapi.infrastructure.persistence.jpa.mapper;

import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.infrastructure.persistence.jpa.entity.BookEntity;

public class BookMapper {
    public static Book toDomain(BookEntity entity) {
        return new Book(
                entity.getId(),
                Isbn.of(entity.getIsbn()),
                entity.getTitle(),
                entity.getSubtitle(),
                entity.getImage(),
                entity.getAuthorId(),
                entity.getPublisherId(),
                entity.getPublished(),
                new AuditInfo(
                        entity.getCreatedAt(),
                        entity.getCreatedBy(),
                        entity.getUpdatedAt(),
                        entity.getUpdatedBy()
                )
        );
    }

    public static BookEntity toEntity(Book book) {
        return new BookEntity(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getSubtitle(),
                book.getImage(),
                book.getPublished(),
                book.getPublisherId(),
                book.getAuthorId(),
                book.getAuditInfo() == null? null :  book.getAuditInfo().getCreatedAt(),
                book.getAuditInfo() == null? null :  book.getAuditInfo().getUpdatedAt(),
                book.getAuditInfo() == null? null :  book.getAuditInfo().getCreatedBy(),
                book.getAuditInfo() == null? null :  book.getAuditInfo().getUpdatedBy()
        );
    }
}