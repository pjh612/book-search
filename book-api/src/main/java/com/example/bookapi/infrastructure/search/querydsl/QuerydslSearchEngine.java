package com.example.bookapi.infrastructure.search.querydsl;

import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.infrastructure.search.model.KeywordToken;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import com.example.bookapi.infrastructure.search.parser.QueryParser;
import com.example.bookapi.infrastructure.search.querydsl.predicate.SearchPredicateApplier;
import com.example.bookapi.infrastructure.search.querydsl.predicate.SearchPredicateApplierFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public class QuerydslSearchEngine<T> implements SearchEnginePort<T> {
    private final JPAQueryFactory queryFactory;
    private final QueryParser<SearchCriteria> queryParser;
    private final Function<JPAQueryFactory, JPAQuery<T>> baseQueryFunction;
    private final SearchPredicateApplierFactory searchPredicateApplierFactory;

    public QuerydslSearchEngine(JPAQueryFactory queryFactory, Function<JPAQueryFactory, JPAQuery<T>> baseQueryFunction, QueryParser<SearchCriteria> queryParser, SearchPredicateApplierFactory searchPredicateApplierFactory) {
        this.queryFactory = queryFactory;
        this.baseQueryFunction = baseQueryFunction;
        this.queryParser = queryParser;
        this.searchPredicateApplierFactory = searchPredicateApplierFactory;
    }

    @Override
    public SearchResult<T> search(String keyword, Pageable pageable) {
        long start = System.currentTimeMillis();

        SearchCriteria searchCriteria = queryParser.parse(keyword);

        JPAQuery<T> query = createQuery(searchCriteria);
        Long total = getTotal(query);
        List<T> content = getContent(query, pageable);
        Page<T> page = new PageImpl<>(content, pageable, total);

        long elapsed = System.currentTimeMillis() - start;

        return SearchResult.of(keyword, page, elapsed, searchCriteria.strategy());
    }

    private JPAQuery<T> createQuery(SearchCriteria searchCriteria) {
        JPAQuery<T> baseQuery = baseQueryFunction.apply(queryFactory);

        return applyPredicates(searchCriteria, baseQuery);
    }

    private JPAQuery<T> applyPredicates(SearchCriteria searchCriteria, JPAQuery<T> baseQuery) {
        BooleanBuilder builder = new BooleanBuilder();
        for (KeywordToken token : searchCriteria.tokens()) {
            SearchPredicateApplier provider = searchPredicateApplierFactory.getApplier(token.operator());
            builder = provider.apply(builder, token.keyword());
        }

        return baseQuery.where(builder);
    }

    private Long getTotal(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        clone.getMetadata().clearOrderBy();

        return clone.select(Wildcard.count)
                .fetchOne();
    }

    private List<T> getContent(JPAQuery<T> query, Pageable pageable) {
        return query.limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
