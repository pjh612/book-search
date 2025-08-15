package com.example.trevaribooksearch.infrastructure.web;

import com.example.trevaribooksearch.application.dto.BookDetailResponse;
import com.example.trevaribooksearch.application.dto.BookResponse;
import com.example.trevaribooksearch.application.in.QueryBookUseCase;
import com.example.trevaribooksearch.domain.model.Isbn;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QueryBookUseCase queryBookUseCase;

    @Test
    @DisplayName("/api/books GET 요청시 200 OK와 결과 반환")
    void getBooks_returnsOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        BookResponse book = new BookResponse(
                UUID.randomUUID(),
                "9781234567890",
                "테스트 책",
                "부제목",
                null,
                Isbn.randomIsbn13(),
                "테스트 출판사",
                Instant.now()
        );
        List<BookResponse> content = Collections.singletonList(book);

        Mockito.when(queryBookUseCase.findBooks(Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(content, pageable, 1));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(book.id().toString()))
                .andExpect(jsonPath("$.content[0].isbn").value(book.isbn()))
                .andExpect(jsonPath("$.content[0].title").value(book.title()))
                .andExpect(jsonPath("$.content[0].subtitle").value(book.subtitle()))
                .andExpect(jsonPath("$.content[0].author").value(book.author()))
                .andExpect(jsonPath("$.content[0].publisher").value(book.publisher()))
                .andExpect(jsonPath("$.content[0].published").value(book.published().toString()));

    }

    @Test
    @DisplayName("/api/books/{id} GET 요청시 200 OK와 결과 반환")
    void getBookById_returnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        BookDetailResponse response = new BookDetailResponse(
                id,
                "테스트 책",
                "부제목",
                "저자",
                null,
                "9781234567890",
                "테스트 출판사",
                Instant.now(),
                "admin",
                "admin",
                Instant.now(),
                Instant.now()
        );

        Mockito.when(queryBookUseCase.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.subtitle").value(response.subtitle()))
                .andExpect(jsonPath("$.author").value(response.author()))
                .andExpect(jsonPath("$.isbn").value(response.isbn()))
                .andExpect(jsonPath("$.publisher").value(response.publisher()))
                .andExpect(jsonPath("$.createdBy").value(response.createdBy()))
                .andExpect(jsonPath("$.updatedBy").value(response.updatedBy()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()))
                .andExpect(jsonPath("$.updatedAt").value(response.updatedAt().toString()));
    }

    @Test
    @DisplayName("/api/books/{id} GET 요청시 존재하지 않으면 404 반환")
    void getBookById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(queryBookUseCase.findById(id))
                .thenThrow(new EntityNotFoundException("도서 정보를 찾을 수 없습니다. ID: " + id));

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isNotFound());
    }
}