package com.example.keywordconsumerservice.infrastructure.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3L);

        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate), fixedBackOff);
    }
}
