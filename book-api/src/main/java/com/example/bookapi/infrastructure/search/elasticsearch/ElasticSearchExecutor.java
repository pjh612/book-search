package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.data.domain.Pageable;

public interface ElasticSearchExecutor<T> {
    SearchResponse<T> execute(String indexName, Class<T> documentType, Query query, Pageable pageable);
}

