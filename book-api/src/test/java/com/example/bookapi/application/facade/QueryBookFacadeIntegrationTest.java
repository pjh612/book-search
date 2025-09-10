package com.example.bookapi.application.facade;

import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.event.SearchEvent;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.application.out.MessagePublisher;
import com.example.bookapi.infrastructure.cache.redis.TestRedisConfiguration;
import com.example.bookapi.infrastructure.search.elasticsearch.TestElasticSearchConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestRedisConfiguration.class, TestElasticSearchConfig.class})
class QueryBookFacadeIntegrationTest {

    @Autowired
    QueryBookFacade queryBookFacade;

    @MockitoBean
    MessagePublisher messagePublisher;

    @Autowired
    QueryBookUseCase queryBookUseCase;

    @Test
    @DisplayName("캐시 적중 여부와 무관하게 이벤트가 항상 발행")
    void searchBooks_should_event_publish_always() {
        // given
        BookSearchRequest request = new BookSearchRequest("테스트", PageRequest.of(0, 10));

        // when: 첫 번째 호출 (캐시 미적중)
        queryBookFacade.searchBooks(request);
        // when: 두 번째 호출 (캐시 적중)
        queryBookFacade.searchBooks(request);

        // then: 이벤트는 두 번 모두 발행되어야 함
        verify(messagePublisher, times(2)).publish(eq("search-keyword"), any(SearchEvent.class));
    }
}