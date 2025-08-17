package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.application.out.HotKeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QueryHotKeywordServiceTest {

    @Test
    @DisplayName("핫 키워드 목록을 반환한다")
    void find_returnsHotKeywords() {
        // given
        HotKeywordRepository hotKeywordRepository = Mockito.mock(HotKeywordRepository.class);
        List<String> keywords = List.of("자바", "스프링", "JPA");
        LocalDate now = LocalDate.now();
        Mockito.when(hotKeywordRepository.find(3, now)).thenReturn(keywords);

        QueryHotKeywordService service = new QueryHotKeywordService(hotKeywordRepository);

        // when
        HotKeywordResponse response = service.find(3, now);

        // then
        assertThat(response.keywords()).containsExactly("자바", "스프링", "JPA");
        Mockito.verify(hotKeywordRepository).find(3, now);
    }

    @Test
    @DisplayName("핫 키워드가 없으면 빈 리스트를 반환한다")
    void find_returnsEmptyList_whenNoKeywords() {
        // given
        HotKeywordRepository hotKeywordRepository = Mockito.mock(HotKeywordRepository.class);
        LocalDate now = LocalDate.now();
        Mockito.when(hotKeywordRepository.find(5, now)).thenReturn(List.of());

        QueryHotKeywordService service = new QueryHotKeywordService(hotKeywordRepository);

        // when
        HotKeywordResponse response = service.find(5, now);

        // then
        assertThat(response.keywords()).isEmpty();
        Mockito.verify(hotKeywordRepository).find(5, now);
    }

}