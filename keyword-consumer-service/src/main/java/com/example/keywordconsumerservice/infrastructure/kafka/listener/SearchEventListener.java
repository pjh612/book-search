package com.example.keywordconsumerservice.infrastructure.kafka.listener;

import com.example.keywordconsumerservice.application.in.RegisterHotKeywordUseCase;
import com.example.keywordconsumerservice.domain.SearchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchEventListener {
    private final RegisterHotKeywordUseCase registerSearchKeywordUseCase;

    @KafkaListener(topics = "search-keyword", groupId = "hot-keyword", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload SearchEvent payload) {
        log.debug("Message received: {}", payload);

        registerSearchKeywordUseCase.registerKeyword(payload.keyword());
    }
}
