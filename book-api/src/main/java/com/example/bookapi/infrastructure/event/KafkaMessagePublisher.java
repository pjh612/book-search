package com.example.bookapi.infrastructure.event;

import com.example.bookapi.application.out.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessagePublisher implements MessagePublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(String topic, Object message) {
        kafkaTemplate.send(topic, message);
        log.debug("Message Published to topic: {}, message: {}", topic, message);
    }
}
