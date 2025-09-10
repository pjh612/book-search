package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.bookapi.infrastructure.search.exception.SearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class DefaultElasticSearchExecutor<T> implements ElasticSearchExecutor<T> {
    private final ElasticsearchClient client;

    private static final Logger log = LoggerFactory.getLogger(DefaultElasticSearchExecutor.class);

    public DefaultElasticSearchExecutor(ElasticsearchClient client) {
        this.client = client;
    }

    public SearchResponse<T> execute(String indexName, Class<T> documentType, Query query, Pageable pageable) {
        if (log.isDebugEnabled()) {
            log.debug("Executing search on index: {}, page: {}", indexName, pageable);
        }

        SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
                .index(indexName)
                .query(query)
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            requestBuilder.sort(s -> s
                    .field(f -> f
                            .field(order.getProperty())
                            .order(order.isAscending() ? SortOrder.Asc : SortOrder.Desc)
                    )
            );
        }
        try {
            return client.search(requestBuilder.build(), documentType);
        } catch (IOException | ElasticsearchException e) {
            throw new SearchException(e);
        }
    }
}
