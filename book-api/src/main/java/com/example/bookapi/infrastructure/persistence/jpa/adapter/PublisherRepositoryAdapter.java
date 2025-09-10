package com.example.bookapi.infrastructure.persistence.jpa.adapter;

import com.example.bookapi.domain.model.Publisher;
import com.example.bookapi.domain.repository.PublisherRepository;
import com.example.bookapi.infrastructure.persistence.jpa.entity.PublisherEntity;
import com.example.bookapi.infrastructure.persistence.jpa.mapper.PublisherMapper;
import com.example.bookapi.infrastructure.persistence.jpa.repository.PublisherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Publisher> findById(UUID id) {
        return publisherRepository.findById(id)
                .map(PublisherMapper::toDomain);
    }
}
