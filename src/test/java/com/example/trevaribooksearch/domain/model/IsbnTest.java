package com.example.trevaribooksearch.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IsbnTest {

    @Test
    @DisplayName("ISBN-13, ISBN-10 정상 생성")
    void of_valid_isbn() {
        assertThat(Isbn.of("9781234567890").value()).isEqualTo("9781234567890");
        assertThat(Isbn.of("0-306-40615-2").value()).isEqualTo("0306406152");
        assertThat(Isbn.of("123456789X").value()).isEqualTo("123456789X");
        assertThat(Isbn.of("123456789x").value()).isEqualTo("123456789X");
        assertThat(Isbn.of(" 978-1-2345-6789-0 ").value()).isEqualTo("9781234567890");
    }

    @Test
    @DisplayName("잘못된 ISBN 입력 시 예외 발생")
    void of_invalid_isbn() {
        assertThatThrownBy(() -> Isbn.of("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Isbn.of("123")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Isbn.of("abcdefghijk")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Isbn.of(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("equals, hashCode 동작 확인")
    void equals_and_hashCode() {
        Isbn a = Isbn.of("9781234567890");
        Isbn b = Isbn.of("978-1234567890");
        Isbn c = Isbn.of("9781234567891");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isNotEqualTo(c);
        assertThat(a).isNotEqualTo("9781234567890"); // 타입 다름
        assertThat(a).isNotEqualTo(null);
    }

    @Test
    @DisplayName("toString은 value와 같다")
    void toString_returns_value() {
        Isbn isbn = Isbn.of("9781234567890");
        assertThat(isbn.toString()).isEqualTo(isbn.value());
    }

    @Test
    @DisplayName("randomIsbn13은 유효한 ISBN-13을 반환한다")
    void randomIsbn13_valid() {
        for (int i = 0; i < 10; i++) {
            String isbn = Isbn.randomIsbn13();
            assertThat(isbn).matches("^(978|979)\\d{10}$");
            assertThatCode(() -> Isbn.of(isbn)).doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("equals: 동일 인스턴스는 항상 true")
    void equals_same_instance() {
        Isbn isbn = Isbn.of("9781234567890");
        assertThat(isbn.equals(isbn)).isTrue();
    }
}