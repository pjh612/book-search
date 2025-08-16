package com.example.trevaribooksearch.infrastructure.persistence.jpa.repository;

import com.example.trevaribooksearch.application.dto.BookDetailResponse;
import com.example.trevaribooksearch.application.dto.BookResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.QAuthorEntity.authorEntity;
import static com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.QBookEntity.bookEntity;
import static com.example.trevaribooksearch.infrastructure.persistence.jpa.entity.QPublisherEntity.publisherEntity;

@Repository
public class BookQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final Map<String, ComparableExpressionBase<?>> sortMap = new HashMap<>();

    public BookQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        sortMap.put("createdAt", bookEntity.createdAt);
    }

    public Page<BookResponse> findBooks(Pageable pageRequest) {
        List<BookResponse> fetch = queryFactory.select(Projections.constructor(BookResponse.class,
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
                .join(publisherEntity).on(publisherEntity.id.eq(bookEntity.publisherId))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getOrderSpecifiers(pageRequest.getSort()))
                .fetch();

        Long total = queryFactory.select(bookEntity.count())
                .from(bookEntity)
                .fetchOne();

        return new PageImpl<>(fetch, pageRequest, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        if (sort.isUnsorted()) {
            return new OrderSpecifier[]{bookEntity.createdAt.desc()};
        }
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            ComparableExpressionBase<?> expression = sortMap.get(property);
            if (expression == null) {
                continue;
            }
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            orders.add(new OrderSpecifier(direction, expression));
        }
        if (orders.isEmpty()) {
            orders.add(bookEntity.createdAt.desc());
        }
        return orders.toArray(new OrderSpecifier[0]);
    }

    public Optional<BookDetailResponse> findById(UUID id) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(BookDetailResponse.class,
                                bookEntity.id,
                                bookEntity.title,
                                bookEntity.subtitle,
                                authorEntity.name,
                                bookEntity.image,
                                bookEntity.isbn,
                                publisherEntity.name,
                                bookEntity.published,
                                bookEntity.createdBy,
                                bookEntity.updatedBy,
                                bookEntity.createdAt,
                                bookEntity.updatedAt
                        )
                )
                .from(bookEntity)
                .join(authorEntity).on(bookEntity.authorId.eq(authorEntity.id))
                .join(publisherEntity).on(publisherEntity.id.eq(bookEntity.publisherId))
                .where(bookEntity.id.eq(id))
                .fetchOne());
    }
}
