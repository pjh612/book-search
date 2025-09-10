package com.example.bookapi.infrastructure.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.io.IOException;

@TestConfiguration
public class TestBookIndexConfig {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void init() throws IOException {
        // 인덱스 생성 및 매핑 설정
        createBookIndex();
    }

    private void createBookIndex() throws IOException {
        try {
            if (elasticsearchClient.indices().exists(e -> e.index("book")).value()) {
                return;
            }

            elasticsearchClient.indices().create(c -> c
                    .index("book")
                    .mappings(m -> m
                            .properties("id", p -> p.keyword(k -> k))
                            .properties("isbn", p -> p.keyword(k -> k))
                            .properties("title", p -> p.text(t -> t))
                            .properties("subtitle", p -> p.text(t -> t))
                            .properties("image", p -> p.keyword(k -> k))
                            .properties("published", p -> p.date(d -> d))
                            .properties("publisherId", p -> p.keyword(k -> k))
                            .properties("publisher", p -> p.text(t -> t))
                            .properties("authorId", p -> p.keyword(k -> k))
                            .properties("author", p -> p.text(t -> t))
                            .properties("createdBy", p -> p.keyword(k -> k))
                            .properties("updatedBy", p -> p.keyword(k -> k))
                            .properties("createdAt", p -> p.date(d -> d))
                            .properties("updatedAt", p -> p.date(d -> d))
                    )
            );
        } catch (ElasticsearchException e) {
            if (e.response().error().type().equals("resource_already_exists_exception")) {
                // 인덱스가 이미 존재하는 경우 무시
                return;
            }
            throw e;
        }
    }
}
