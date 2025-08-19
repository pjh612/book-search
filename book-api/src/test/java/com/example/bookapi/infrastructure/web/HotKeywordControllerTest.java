package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.application.in.QueryHotKeywordUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotKeywordController.class)
@WithMockUser(username = "test")
class HotKeywordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueryHotKeywordUseCase queryHotKeywordUseCase;

    @Test
    @DisplayName("/api/hot-keywords GET 요청시 200 OK와 결과 반환")
    void getHotKeyword_returnsOk() throws Exception {
        int size = 5;
        LocalDate date = LocalDate.of(2024, 6, 1);
        List<String> keywords = List.of(
                "java",
                "spring"
        );
        HotKeywordResponse response = new HotKeywordResponse(keywords);

        Mockito.when(queryHotKeywordUseCase.find(size, date)).thenReturn(response);

        mockMvc.perform(get("/api/hot-keywords")
                        .param("size", String.valueOf(size))
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keywords[0]").value("java"))
                .andExpect(jsonPath("$.keywords[1]").value("spring"));
    }

    @Test
    @DisplayName("/api/hot-keywords GET 요청시 결과가 비어있어도 200 OK와 빈 배열 반환")
    void getHotKeyword_returnsEmptyList() throws Exception {
        int size = 5;
        LocalDate date = LocalDate.of(2024, 6, 1);
        List<String> keywords = List.of();
        HotKeywordResponse response = new HotKeywordResponse(keywords);

        Mockito.when(queryHotKeywordUseCase.find(size, date)).thenReturn(response);

        mockMvc.perform(get("/api/hot-keywords")
                        .param("size", String.valueOf(size))
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keywords").isArray())
                .andExpect(jsonPath("$.keywords").isEmpty());
    }
}