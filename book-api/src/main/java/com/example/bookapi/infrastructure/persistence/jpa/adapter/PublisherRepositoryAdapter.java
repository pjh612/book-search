package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.domain.model.Publisher;
import com.example.bookapi.domain.repository.PublisherRepository;
import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import com.example.bookapi.infrastructure.persistence.jpa.mapper.PublisherMapper;
import com.example.bookapi.infrastructure.persistence.jpa.repository.PublisherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublisherRepositoryAdapter implements PublisherRepository {
    private final PublisherJpaRepository publisherRepository;

    @Override
    public Publisher save(Publisher publisher) {
        PublisherEntity save = publisherRepository.save(
                PublisherMapper.toEntity(publisher));

        return PublisherMapper.toDomain(save);
    }
}
