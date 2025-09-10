package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.infrastructure.search.exception.InvalidSearchQueryException;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import com.example.bookapi.infrastructure.search.port.QueryParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ElasticSearchEngine<T, R, C extends SearchCriteria> implements SearchEnginePort<R> {
    private final QueryParser<C> queryParser;
    private final QueryBuilder<C> queryBuilder;
    private final ElasticSearchExecutor<T> executor;
    private final SearchResultAggregator<T, R> resultAggregator;
    private final Class<T> documentType;
    private final String indexName;

    @Override
    public SearchResult<R> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            throw new InvalidSearchQueryException("Search keyword must not be null");
        }
        if (pageable == null) {
            throw new InvalidSearchQueryException("Pageable must not be null for search: " + keyword);
        }

        keyword = keyword.trim();

        long start = System.currentTimeMillis();
        C criteria = queryParser.parse(keyword);
        Query query = queryBuilder.build(criteria);
        SearchResponse<T> searchResponse = executor.execute(indexName, documentType, query, pageable);
        long elapsed = System.currentTimeMillis() - start;

        return resultAggregator.mapToResult(keyword, pageable, searchResponse, elapsed, criteria.strategy());
    }
}
