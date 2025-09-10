package com.example.bookapi.infrastructure.search.elasticsearch.operator;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.example.bookapi.infrastructure.search.port.SearchOperatorApplier;

@FunctionalInterface
public interface ElasticSearchOperatorApplier extends SearchOperatorApplier<BoolQuery.Builder> {
}
