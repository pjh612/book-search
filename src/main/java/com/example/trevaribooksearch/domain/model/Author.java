package com.example.trevaribooksearch.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author {
    private UUID id;
    private String name;
    private AuditInfo auditInfo;

    public Author(UUID id, String name, AuditInfo auditInfo) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Author name is required");
        this.id = id;
        this.name = name.trim();
        this.auditInfo = auditInfo;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("name required");
        this.name = newName.trim();
    }
}

