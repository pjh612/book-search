package com.example.bookapi.infrastructure.search.elasticsearch.repository;

import com.example.bookapi.infrastructure.search.elasticsearch.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, UUID> {
}
