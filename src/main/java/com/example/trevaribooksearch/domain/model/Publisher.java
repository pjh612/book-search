package com.example.trevaribooksearch.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Publisher {
    private UUID id;
    private String name;
    private AuditInfo auditInfo;


    public Publisher(UUID id, String name, AuditInfo auditInfo) {
        this.id = id;
        this.name = name;
        this.auditInfo = auditInfo;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Publisher && Objects.equals(id, ((Publisher) o).id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
