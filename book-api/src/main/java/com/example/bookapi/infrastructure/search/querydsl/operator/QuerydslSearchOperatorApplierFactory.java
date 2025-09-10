package com.example.bookapi.infrastructure.search.querydsl.operator;

import com.example.bookapi.infrastructure.search.port.SearchOperatorApplierFactory;
import com.example.bookapi.infrastructure.search.model.SearchOperator;

import java.util.Map;

public class QuerydslSearchOperatorApplierFactory implements SearchOperatorApplierFactory<QuerydslSearchOperatorApplier> {
    private final Map<SearchOperator, QuerydslSearchOperatorApplier> appliers;

    public QuerydslSearchOperatorApplierFactory(Map<SearchOperator, QuerydslSearchOperatorApplier> appliers) {
        this.appliers = appliers;
    }

    public QuerydslSearchOperatorApplier getApplier(SearchOperator operator) {
        QuerydslSearchOperatorApplier applier = appliers.get(operator);
        if (applier == null) {
            throw new IllegalArgumentException("No applier found for strategy: " + operator.name());
        }

        return applier;
    }
}
