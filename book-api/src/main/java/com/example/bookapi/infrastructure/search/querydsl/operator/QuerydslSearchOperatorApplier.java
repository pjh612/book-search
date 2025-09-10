package com.example.bookapi.infrastructure.search.querydsl.operator;

import com.example.bookapi.infrastructure.search.port.SearchOperatorApplier;
import com.querydsl.core.BooleanBuilder;

@FunctionalInterface
public interface QuerydslSearchOperatorApplier extends SearchOperatorApplier<BooleanBuilder> {
    BooleanBuilder apply(BooleanBuilder booleanBuilder, String keyword);
}
