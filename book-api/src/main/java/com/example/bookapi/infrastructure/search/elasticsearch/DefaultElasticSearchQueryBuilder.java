package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.example.bookapi.infrastructure.search.elasticsearch.operator.ElasticSearchOperatorApplierFactory;
import com.example.bookapi.infrastructure.search.elasticsearch.operator.ElasticSearchOperatorApplier;
import com.example.bookapi.infrastructure.search.model.KeywordToken;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;

public class DefaultElasticSearchQueryBuilder implements QueryBuilder<SearchCriteria> {
    private final ElasticSearchOperatorApplierFactory applierFactory;

    public DefaultElasticSearchQueryBuilder(ElasticSearchOperatorApplierFactory applierFactory) {
        this.applierFactory = applierFactory;
    }

    @Override
    public Query build(SearchCriteria searchCriteria) {
        BoolQuery.Builder builder = QueryBuilders.bool();
        if (searchCriteria.tokens().isEmpty()) {
            builder.must(QueryBuilders.matchAll().build()._toQuery());
        } else {
            for (KeywordToken token : searchCriteria.tokens()) {
                ElasticSearchOperatorApplier applier = applierFactory.getApplier(token.operator());
                applier.apply(builder, token.keyword());
            }
        }
        return builder.build()._toQuery();
    }
}
