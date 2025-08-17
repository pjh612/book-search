package com.example.keywordconsumerservice.infrastructure.kafka.listener;

import com.example.keywordconsumerservice.application.in.RegisterHotKeywordUseCase;
import com.example.keywordconsumerservice.domain.SearchEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"search-keyword", "search-keyword-dlt"})
@ActiveProfiles("test")
@Import(ObjectMapper.class)
class SearchEventListenerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoBean
    private RegisterHotKeywordUseCase registerHotKeywordUseCase;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("SearchEvent가 kafka로 전송되면 핫 키워드가 등록된다.")
    void kafkaMessage_consumedAndHandled() {
        // given
        SearchEvent event = new SearchEvent("spring");

        // when
        kafkaTemplate.send("search-keyword", event);

        // then
        verify(registerHotKeywordUseCase, timeout(5000)).registerKeyword("spring");
    }

    @Test
    @DisplayName("consumer에서 오류가 발생하면 dlt로 메시지가 전송된다.")
    void message_is_sent_to_dlt_on_exception() throws Exception {
        // given
        doThrow(new RuntimeException("fail")).when(registerHotKeywordUseCase).registerKeyword("spring");
        SearchEvent event = new SearchEvent("spring");

        // when
        kafkaTemplate.send("search-keyword", event);

        //then
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-dlt-group", "false", embeddedKafka);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", JsonDeserializer.class);

        try (Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer()) {
            embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "search-keyword-dlt");
            ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofMillis(5000));
            assertThat(records.count()).isGreaterThan(0);
            SearchEvent searchEvent = objectMapper.readValue(records.iterator().next().value(), SearchEvent.class);
            assertThat(searchEvent.keyword()).isEqualTo("spring");
        }
    }
}