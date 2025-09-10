package com.example.bookapi.infrastructure.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.out.SearchEnginePort;
import com.example.bookapi.infrastructure.search.elasticsearch.DefaultElasticSearchExecutor;
import com.example.bookapi.infrastructure.search.elasticsearch.DefaultElasticSearchResultAggregator;
import com.example.bookapi.infrastructure.search.port.SearchResultMapper;
import com.example.bookapi.infrastructure.search.elasticsearch.QueryBuilder;
import com.example.bookapi.infrastructure.search.elasticsearch.ElasticSearchEngine;
import com.example.bookapi.infrastructure.search.elasticsearch.ElasticSearchExecutor;
import com.example.bookapi.infrastructure.search.elasticsearch.SearchResultAggregator;
import com.example.bookapi.infrastructure.search.elasticsearch.DefaultElasticSearchQueryBuilder;
import com.example.bookapi.infrastructure.search.elasticsearch.document.BookDocument;
import com.example.bookapi.infrastructure.search.elasticsearch.operator.ElasticSearchOperatorApplierFactory;
import com.example.bookapi.infrastructure.search.elasticsearch.operator.ElasticSearchOperatorApplier;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchOperator;
import com.example.bookapi.infrastructure.search.model.SearchOperatorType;
import com.example.bookapi.infrastructure.search.parser.DefaultQueryParser;
import com.example.bookapi.infrastructure.search.port.QueryParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SearchEngineConfig {
    @Bean
    SearchEnginePort<BookResponse> searchEnginePort(QueryParser<SearchCriteria> queryParser,
                                                    QueryBuilder<SearchCriteria> queryBuilder,
                                                    ElasticSearchExecutor<BookDocument> searchExecutor,
                                                    SearchResultAggregator<BookDocument, BookResponse> resultAggregator
    ) {
        return new ElasticSearchEngine<>(
                queryParser,
                queryBuilder,
                searchExecutor,
                resultAggregator,
                BookDocument.class,
                "book"
        );
    }

    @Bean
    QueryParser<SearchCriteria> queryParser() {
        return new DefaultQueryParser();
    }

    @Bean
    QueryBuilder<SearchCriteria> elasticQueryBuilder(ElasticSearchOperatorApplierFactory esPredicateApplierFactory) {
        return new DefaultElasticSearchQueryBuilder(esPredicateApplierFactory);
    }

    @Bean
    ElasticSearchExecutor<BookDocument> bookDocumentElasticSearchExecutor(ElasticsearchClient elasticsearchClient) {
        return new DefaultElasticSearchExecutor<>(elasticsearchClient);
    }

    @Bean
    SearchResultAggregator<BookDocument, BookResponse> resultAggregator() {
        SearchResultMapper<BookDocument, BookResponse> searchResultMapper = document -> new BookResponse(
                document.getId(),
                document.getTitle(),
                document.getSubtitle(),
                document.getAuthor(),
                document.getImage(),
                document.getIsbn(),
                document.getPublisher(),
                document.getPublished()
        );
        return new DefaultElasticSearchResultAggregator<>(searchResultMapper);
    }

    @Bean
    ElasticSearchOperatorApplierFactory elasticSearchOperatorApplierFactory() {
        ElasticSearchOperatorApplier noAndOrPredicateApplier = (builder, keyword) -> builder.should(
                QueryBuilders.match(m -> m.field("title").query(keyword)),
                QueryBuilders.match(m -> m.field("subtitle").query(keyword))
        );
        ElasticSearchOperatorApplier notPredicateApplier = (builder, keyword) -> builder.mustNot(
                QueryBuilders.match(m -> m.field("title").query(keyword)),
                QueryBuilders.match(m -> m.field("subtitle").query(keyword))
        );
        Map<SearchOperator, ElasticSearchOperatorApplier> operatorMap = Map.of(
                SearchOperatorType.NO_OPERATOR, noAndOrPredicateApplier,
                SearchOperatorType.OR_OPERATOR, noAndOrPredicateApplier,
                SearchOperatorType.NOT_OPERATOR, notPredicateApplier
        );

        return new ElasticSearchOperatorApplierFactory(operatorMap);
    }

    @Bean
    RestClientTransport restClientTransport(RestClient restClient, ObjectProvider<RestClientOptions> restClientOptions) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return new RestClientTransport(restClient, new JacksonJsonpMapper(mapper), restClientOptions.getIfAvailable());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClientTransport restClientTransport) {
        return new ElasticsearchClient(restClientTransport);
    }
}
