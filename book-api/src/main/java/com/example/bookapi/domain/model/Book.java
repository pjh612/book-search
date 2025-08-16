package com.example.bookapi.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    private UUID id;
    private Isbn isbn;
    private String title;
    private String subtitle;
    private String image;
    private UUID authorId;
    private UUID publisherId;
    private Instant published;
    private AuditInfo auditInfo;

    public String getIsbn() {
        return isbn.value();
    }

    public Book(UUID id, Isbn isbn, String title, String subtitle, String image, UUID authorId, UUID publisherId, Instant published, AuditInfo auditInfo) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        if (isbn == null) throw new IllegalArgumentException("ISBN is required");
        if (authorId == null) throw new IllegalArgumentException("Author ID is required");
        if (publisherId == null) throw new IllegalArgumentException("Publisher ID is required");

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle != null ? subtitle.trim() : null;
        this.image = image;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.published = published;
        this.auditInfo = auditInfo;
    }

    public static Book create(String title, String subtitle, String image, String isbn, UUID authorId, UUID publisherId, Instant published, String createdBy) {
        return new Book(
                UUID.randomUUID(),
                Isbn.of(isbn),
                title.trim(),
                subtitle,
                image,
                authorId,
                publisherId,
                published,
                AuditInfo.create(createdBy)
        );
    }
}
