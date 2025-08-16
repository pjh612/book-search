package com.example.bookapi.domain.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class AuditInfo {
    private final Instant createdAt;
    private final String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    public static AuditInfo create(String createdBy) {
        return new AuditInfo(Instant.now(), createdBy, null, null);
    }

    public void update(String updatedBy) {
        this.updatedAt = Instant.now();
        this.updatedBy = updatedBy;
    }

    public AuditInfo(Instant createdAt, String createdBy, Instant updatedAt, String updatedBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}