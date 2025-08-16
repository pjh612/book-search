package com.example.trevaribooksearch.infrastructure.persistence.jpa.mapper;

import com.example.trevaribooksearch.domain.model.AuditInfo;
import com.example.trevaribooksearch.domain.model.Publisher;
import com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.PublisherEntity;

public class PublisherMapper {
    public static Publisher toDomain(PublisherEntity entity) {
        return new Publisher(
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

    public static PublisherEntity toEntity(Publisher publisher) {
        return new PublisherEntity(
                publisher.getId(),
                publisher.getName(),
                publisher.getAuditInfo() == null? null : publisher.getAuditInfo().getCreatedAt(),
                publisher.getAuditInfo() == null? null :publisher.getAuditInfo().getUpdatedAt(),
                publisher.getAuditInfo() == null? null :publisher.getAuditInfo().getCreatedBy(),
                publisher.getAuditInfo() == null? null :publisher.getAuditInfo().getUpdatedBy()
        );
    }
}