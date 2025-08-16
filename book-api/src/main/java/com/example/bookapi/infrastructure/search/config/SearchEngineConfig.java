package com.example.bookapi.infrastructure.search.config;

import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.infrastructure.persistence.jpa.entity.QBookEntity;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchOperatorType;
import com.example.bookapi.infrastructure.search.parser.DefaultQueryParser;
import com.example.bookapi.infrastructure.search.parser.QueryParser;
import com.example.bookapi.infrastructure.search.querydsl.QuerydslSearchEngine;
import com.example.bookapi.infrastructure.search.querydsl.predicate.SearchPredicateApplier;
import com.example.bookapi.infrastructure.search.querydsl.predicate.SearchPredicateApplierFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;

import static com.example.bookapi.infrastructure.persistence.jpa.entity.QAuthorEntity.authorEntity;
import static com.example.bookapi.infrastructure.persistence.jpa.entity.QBookEntity.bookEntity;
import static com.example.bookapi.infrastructure.persistence.jpa.entity.QPublisherEntity.publisherEntity;

@Configuration
public class SearchEngineConfig {
    @Bean
    QueryParser<SearchCriteria> queryParser() {
        return new DefaultQueryParser();
    }

    @Bean("bookSearchPredicateApplierFactory")
    SearchPredicateApplierFactory bookSearchPredicateApplierFactory() {
        Map<SearchOperator, SearchPredicateApplier> operatorMap = Map.of(
                SearchOperatorType.NO_OPERATOR, (booleanBuilder, keyword) -> booleanBuilder.or(QBookEntity.bookEntity.title.like("%" + keyword + "%")),
                SearchOperatorType.OR_OPERATOR, (booleanBuilder, keyword) -> booleanBuilder.or(QBookEntity.bookEntity.title.like("%" + keyword + "%")),
                SearchOperatorType.NOT_OPERATOR, (booleanBuilder, keyword) -> booleanBuilder.and(QBookEntity.bookEntity.title.notLike("%" + keyword + "%"))
        );

        return new SearchPredicateApplierFactory(operatorMap);
    }

    @Bean
    SearchEnginePort<BookResponse> bookSearchEngine(JPAQueryFactory jpaQueryFactory, QueryParser<SearchCriteria> queryParser, SearchPredicateApplierFactory factory) {
        return new QuerydslSearchEngine<>(jpaQueryFactory, baseQuery(), queryParser, factory);
    }

    private static Function<JPAQueryFactory, JPAQuery<BookResponse>> baseQuery() {
        return (jpaQueryFactory) -> jpaQueryFactory.select(Projections.constructor(BookResponse.class,
                                bookEntity.id,
                                bookEntity.title,
                                bookEntity.subtitle,
                                authorEntity.name,
                                bookEntity.image,
                                bookEntity.isbn,
                                publisherEntity.name,
                                bookEntity.published
                        )
                )
                .from(bookEntity)
                .join(authorEntity).on(bookEntity.authorId.eq(authorEntity.id))
                .join(publisherEntity).on(publisherEntity.id.eq(bookEntity.publisherId));
    }
}
