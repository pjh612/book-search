package com.example.bookapi.infrastructure.search.parser;

import com.example.bookapi.infrastructure.search.exception.InvalidSearchQueryException;
import com.example.bookapi.infrastructure.search.model.KeywordToken;
import com.example.bookapi.infrastructure.search.model.SearchCriteria;
import com.example.bookapi.infrastructure.search.model.SearchOperatorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultQueryParserTest {
    private final DefaultQueryParser parser = new DefaultQueryParser();

    @Test
    @DisplayName("정상 쿼리 파싱 시 KeywordToken 리스트를 반환한다")
    void parse_success() {
        String query = "java -spring |kotlin";
        SearchCriteria criteria = parser.parse(query);

        List<KeywordToken> tokens = criteria.tokens();
        assertThat(tokens).hasSize(3);
        assertThat(tokens.get(0).keyword()).isEqualTo("java");
        assertThat(tokens.get(0).operator()).isEqualTo(SearchOperatorType.NO_OPERATOR);

        assertThat(tokens.get(1).keyword()).isEqualTo("spring");
        assertThat(tokens.get(1).operator()).isEqualTo(SearchOperatorType.NOT_OPERATOR);

        assertThat(tokens.get(2).keyword()).isEqualTo("kotlin");
        assertThat(tokens.get(2).operator()).isEqualTo(SearchOperatorType.OR_OPERATOR);
    }

    @Test
    @DisplayName("빈 문자열 입력 시 InvalidSearchQueryException이 발생한다")
    void parse_blank_throwsException() {
        assertThrows(InvalidSearchQueryException.class, () -> parser.parse(""));
    }

    @Test
    @DisplayName("null 입력 시 InvalidSearchQueryException이 발생한다")
    void parse_null_throwsException() {
        assertThrows(InvalidSearchQueryException.class, () -> parser.parse(null));
    }

    @Test
    @DisplayName("연산자만 입력 시 KeywordToken에 그대로 담긴다")
    void parse_onlyOperator_returnsToken() {
        SearchCriteria criteria = parser.parse("-");
        assertThat(criteria.tokens()).hasSize(1);
        assertThat(criteria.tokens().get(0).keyword()).isEqualTo("-");
    }
}