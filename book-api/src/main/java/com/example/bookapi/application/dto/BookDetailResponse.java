package com.example.bookapi.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDetailResponse {
    private UUID id;
    private String title;
    private String subtitle;
    private String author;
    private String image;
    private String isbn;
    private String publisher;
    private Instant published;
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String subtitle() {
        return subtitle;
    }

    public String author() {
        return author;
    }

    public String image() {
        return image;
    }

    public String isbn() {
        return isbn;
    }

    public String publisher() {
        return publisher;
    }

    public Instant published() {
        return published;
    }

    public String createdBy() {
        return createdBy;
    }

    public String updatedBy() {
        return updatedBy;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
