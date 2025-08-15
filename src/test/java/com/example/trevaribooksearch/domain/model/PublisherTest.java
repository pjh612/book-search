package com.example.trevaribooksearch.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class PublisherTest {

    @Test
    @DisplayName("Publisher 생성자는 정상적으로 Publisher를 생성한다")
    void constructor_success() {
        UUID id = UUID.randomUUID();
        Publisher publisher = new Publisher(id, "출판사", null);

        assertThat(publisher.getId()).isEqualTo(id);
        assertThat(publisher.getName()).isEqualTo("출판사");
        assertThat(publisher.getAuditInfo()).isNull();
    }

    @Test
    @DisplayName("동일 id의 Publisher는 equals/hashCode가 같다")
    void equals_and_hashCode() {
        UUID id = UUID.randomUUID();
        Publisher p1 = new Publisher(id, "A", null);
        Publisher p2 = new Publisher(id, "B", null);

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    @DisplayName("다른 id의 Publisher는 equals가 다르다")
    void not_equals_if_different_id() {
        Publisher p1 = new Publisher(UUID.randomUUID(), "A", null);
        Publisher p2 = new Publisher(UUID.randomUUID(), "A", null);

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    @DisplayName("equals: 동일 인스턴스는 항상 true")
    void equals_same_instance() {
        Publisher publisher = new Publisher(UUID.randomUUID(), "출판사", null);
        assertThat(publisher.equals(publisher)).isTrue();
    }

    @Test
    @DisplayName("equals: null은 항상 false")
    void equals_null() {
        Publisher publisher = new Publisher(UUID.randomUUID(), "출판사", null);
        assertThat(publisher.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals: 타입이 다르면 false")
    void equals_different_type() {
        Publisher publisher = new Publisher(UUID.randomUUID(), "출판사", null);
        assertThat(publisher.equals("not a publisher")).isFalse();
    }

    @Test
    @DisplayName("equals: id가 null이면 동등성 비교는 id 기준")
    void equals_null_id() {
        Publisher p1 = new Publisher(null, "A", null);
        Publisher p2 = new Publisher(null, "B", null);
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    @DisplayName("equals: 한쪽만 id가 null이면 false")
    void equals_one_null_id() {
        Publisher p1 = new Publisher(null, "A", null);
        Publisher p2 = new Publisher(UUID.randomUUID(), "A", null);
        assertThat(p1).isNotEqualTo(p2);
    }
}