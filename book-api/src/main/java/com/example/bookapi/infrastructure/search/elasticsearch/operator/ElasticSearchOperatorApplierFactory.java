package com.example.bookapi.infrastructure.search.elasticsearch.operator;

import com.example.bookapi.infrastructure.search.port.SearchOperatorApplierFactory;
import com.example.bookapi.infrastructure.search.model.SearchOperator;

import java.util.Map;

public class ElasticSearchOperatorApplierFactory implements SearchOperatorApplierFactory<ElasticSearchOperatorApplier> {
    private final Map<SearchOperator, ElasticSearchOperatorApplier> appliers;

    public ElasticSearchOperatorApplierFactory(Map<SearchOperator, ElasticSearchOperatorApplier> appliers) {
        this.appliers = appliers;
    }

    public ElasticSearchOperatorApplier getApplier(SearchOperator operator) {
        ElasticSearchOperatorApplier applier = appliers.get(operator);
        if (applier == null) {
            throw new IllegalArgumentException("No applier found for strategy: " + operator.name());
        }

        return applier;
    }
}
