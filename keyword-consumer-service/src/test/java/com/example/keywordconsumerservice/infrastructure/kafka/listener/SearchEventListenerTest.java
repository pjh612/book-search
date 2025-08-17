package com.example.keywordconsumerservice.infrastructure.kafka.listener;

import com.example.keywordconsumerservice.application.in.RegisterHotKeywordUseCase;
import com.example.keywordconsumerservice.domain.SearchEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SearchEventListenerTest {

    @Test
    @DisplayName("SearchEvent가 consume되면 핫 키워드가 등록된다.")
    void consume_callsRegisterKeyword() {
        // given
        RegisterHotKeywordUseCase useCase = Mockito.mock(RegisterHotKeywordUseCase.class);
        SearchEventListener listener = new SearchEventListener(useCase);
        SearchEvent event = new SearchEvent("java");

        // when
        listener.consume(event);

        // then
        Mockito.verify(useCase).registerKeyword("java");
    }
}