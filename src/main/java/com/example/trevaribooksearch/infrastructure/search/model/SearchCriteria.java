package com.example.trevaribooksearch.infrastructure.search.model;

import java.util.List;

public record SearchCriteria(
        List<KeywordToken> tokens,
        SearchOperator strategy
) {
}
