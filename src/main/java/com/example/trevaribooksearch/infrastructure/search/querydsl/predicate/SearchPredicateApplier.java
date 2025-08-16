package com.example.trevaribooksearch.infrastructure.search.querydsl.predicate;

import com.querydsl.core.BooleanBuilder;

@FunctionalInterface
public interface SearchPredicateApplier {
    BooleanBuilder apply(BooleanBuilder booleanBuilder, String keyword);
}
