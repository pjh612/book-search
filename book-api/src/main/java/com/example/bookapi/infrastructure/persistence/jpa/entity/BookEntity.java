package com.example.bookapi.infrastructure.persistence.jpa.entity;

import com.example.bookapi.infrastructure.persistence.hibernate.annotations.UuidV7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookEntity extends BaseEntity {
    @Id
    @UuidV7Generator
    private UUID id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false, length = 100)
    private String title;

    private String subtitle;

    private String image;

    private Instant published;

    @Column(name = "publisher_id", nullable = false)
    private UUID publisherId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    public BookEntity(UUID id, String isbn, String title, String subtitle, String image, Instant published, UUID publisherId, UUID authorId, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.published = published;
        this.publisherId = publisherId;
        this.authorId = authorId;
    }
}
