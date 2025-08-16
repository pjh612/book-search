package com.example.bookapi.infrastructure.search.querydsl.predicate;

import com.example.bookapi.infrastructure.search.model.SearchOperator;

import java.util.Map;

public class SearchPredicateApplierFactory {
    private final Map<SearchOperator, SearchPredicateApplier> appliers;

    public SearchPredicateApplierFactory(Map<SearchOperator, SearchPredicateApplier> appliers) {
        this.appliers = appliers;
    }

    public SearchPredicateApplier getApplier(SearchOperator operator) {
        SearchPredicateApplier applier = appliers.get(operator);
        if (applier == null) {
            throw new IllegalArgumentException("No applier found for strategy: " + operator.name());
        }

        return applier;
    }
}
