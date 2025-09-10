package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.infrastructure.search.elasticsearch.document.BookDocument;
import com.example.bookapi.infrastructure.search.exception.InvalidSearchQueryException;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchOperatorType;
import com.example.bookapi.infrastructure.search.model.SearchResult;
import com.example.bookapi.infrastructure.search.port.QueryParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("ElasticSearchEngine 단위 테스트")
class ElasticSearchEngineTest {

    @Mock
    private QueryParser<SearchCriteria> queryParser;
    @Mock
    private QueryBuilder<SearchCriteria> queryBuilder;
    @Mock
    private ElasticSearchExecutor<BookDocument> executor;
    @Mock
    private SearchResultAggregator<BookDocument, BookResponse> resultAggregator;

    private ElasticSearchEngine<BookDocument, BookResponse, SearchCriteria> engine;

    @BeforeEach
    void setUp() {
        engine = new ElasticSearchEngine<>(
                queryParser,
                queryBuilder,
                executor,
                resultAggregator,
                BookDocument.class,
                "book"
        );
    }

    @Test
    @DisplayName("검색 요청시 모든 컴포넌트가 올바른 순서로 협력한다")
    void search_shouldDelegateToAllComponentsInCorrectOrder() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "자바";
        SearchCriteria criteria = mock(SearchCriteria.class);
        Query query = mock(Query.class);
        SearchResponse<BookDocument> searchResponse = mock(SearchResponse.class);
        BookResponse bookResponse = new BookResponse(UUID.randomUUID(), "자바와 스프링", "부제", "저자", null, Isbn.randomIsbn13(), "출판사", Instant.now());

        SearchResult<BookResponse> expectedResult = SearchResult.of(
                keyword,
                new PageImpl<>(List.of(bookResponse), pageable, 1),
                10,
                SearchOperatorType.NO_OPERATOR
        );

        given(queryParser.parse(keyword)).willReturn(criteria);
        given(queryBuilder.build(criteria)).willReturn(query);
        given(executor.execute("book", BookDocument.class, query, pageable)).willReturn(searchResponse);
        given(resultAggregator.mapToResult(eq(keyword), eq(pageable), eq(searchResponse), anyLong(), any()))
                .willReturn(expectedResult);

        // when
        SearchResult<BookResponse> result = engine.search(keyword, pageable);

        // then
        assertThat(result).isEqualTo(expectedResult);

        // 호출 순서 검증
        InOrder inOrder = inOrder(queryParser, queryBuilder, executor, resultAggregator);
        inOrder.verify(queryParser).parse(keyword);
        inOrder.verify(queryBuilder).build(criteria);
        inOrder.verify(executor).execute("book", BookDocument.class, query, pageable);
        inOrder.verify(resultAggregator).mapToResult(eq(keyword), eq(pageable), eq(searchResponse), anyLong(), any());
    }

    @Test
    @DisplayName("검색 실행 시간을 정확히 측정한다")
    void search_shouldMeasureExecutionTimeCorrectly() {
        // given
        given(queryParser.parse(any())).willReturn(mock(SearchCriteria.class));
        given(queryBuilder.build(any())).willReturn(mock(Query.class));
        given(executor.execute(any(), any(), any(), any())).willReturn(mock(SearchResponse.class));

        // when
        engine.search("test", PageRequest.of(0, 10));

        // then: 실행 시간이 0ms 이상인지 검증
        verify(resultAggregator).mapToResult(any(), any(), any(), longThat(time -> time >= 0), any());
    }

    @Test
    @DisplayName("정확한 index 이름과 document 타입으로 검색을 실행한다")
    void search_shouldUseCorrectIndexNameAndDocumentType() {
        // given
        given(queryParser.parse(any())).willReturn(mock(SearchCriteria.class));
        given(queryBuilder.build(any())).willReturn(mock(Query.class));
        given(executor.execute(any(), any(), any(), any())).willReturn(mock(SearchResponse.class));

        // when
        engine.search("test", PageRequest.of(0, 10));

        // then: 정확한 indexName과 documentType이 사용되는지 검증
        verify(executor).execute(eq("book"), eq(BookDocument.class), any(), any());
    }

    @Test
    @DisplayName("QueryParser에서 예외가 발생하면后续 컴포넌트는 호출되지 않는다")
    void search_shouldNotCallSubsequentComponentsWhenParserFails() {
        // given
        given(queryParser.parse("invalid"))
                .willThrow(new IllegalArgumentException("Invalid query"));

        // when & then
        assertThatThrownBy(() -> engine.search("invalid", PageRequest.of(0, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid query");

        // 이후 컴포넌트는 호출되지 않아야 함
        verifyNoInteractions(queryBuilder, executor, resultAggregator);
    }

    @Test
    @DisplayName("QueryBuilder에서 예외가 발생하면 Executor와 Aggregator는 호출되지 않는다")
    void search_shouldNotCallExecutorAndAggregatorWhenBuilderFails() {
        // given
        given(queryParser.parse(any())).willReturn(mock(SearchCriteria.class));
        given(queryBuilder.build(any()))
                .willThrow(new IllegalArgumentException("Invalid criteria"));

        // when & then
        assertThatThrownBy(() -> engine.search("test", PageRequest.of(0, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid criteria");

        // 이후 컴포넌트는 호출되지 않아야 함
        verifyNoInteractions(executor, resultAggregator);
    }

    @Test
    @DisplayName("Executor에서 예외가 발생하면 Aggregator는 호출되지 않는다")
    void search_shouldNotCallAggregatorWhenExecutorFails() {
        // given
        given(queryParser.parse(any())).willReturn(mock(SearchCriteria.class));
        given(queryBuilder.build(any())).willReturn(mock(Query.class));
        given(executor.execute(any(), any(), any(), any()))
                .willThrow(new RuntimeException("Elasticsearch error"));

        // when & then
        assertThatThrownBy(() -> engine.search("test", PageRequest.of(0, 10)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Elasticsearch error");

        // aggregator는 호출되지 않아야 함
        verifyNoInteractions(resultAggregator);
    }

    @Test
    @DisplayName("null 키워드로 검색시 InvalidSearchQueryException이 발생한다")
    void search_shouldThrowExceptionWhenKeywordIsNull() {
        // when & then
        assertThatThrownBy(() -> engine.search(null, PageRequest.of(0, 10)))
                .isInstanceOf(InvalidSearchQueryException.class)
                .hasMessageContaining("Search keyword must not be nul");

        verifyNoInteractions(queryParser, queryBuilder, executor, resultAggregator);
    }

    @Test
    @DisplayName("null Pageable로 검색시 InvalidSearchQueryException이 발생한다")
    void search_shouldThrowExceptionWhenPageableIsNull() {
        // when & then
        assertThatThrownBy(() -> engine.search("test", null))
                .isInstanceOf(InvalidSearchQueryException.class)
                .hasMessageContaining("Pageable must not be null for search");

        verifyNoInteractions(queryParser, queryBuilder, executor, resultAggregator);
    }
}