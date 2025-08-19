package com.example.bookweb.client.impl;


import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookApiRestClient implements BookApiClient {
    private final RestClient restClient;

    @Override
    public SignupResponse signup(SignupRequest signupRequest) {
        return restClient.post()
                .uri("/users")
                .body(signupRequest)
                .retrieve()
                .body(SignupResponse.class);
    }

    @Override
    public SigninResponse signin(SigninRequest signinRequest) {
        return restClient.post()
                .uri("/users/auth")
                .body(signinRequest)
                .retrieve()
                .body(SigninResponse.class);

    }

    @Override
    public BookDetailResponse getBookDetailById(UUID id) {
        return restClient.get()
                .uri("/books/{id}", id)
                .retrieve()
                .body(BookDetailResponse.class);
    }

    @Override
    public Page<BookResponse> getBooks(Pageable pageable) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .queryParam("sort", pageable.getSort())
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<PageImpl<BookResponse>>() {
                });
    }

    @Override
    public CursorPageResponse<Instant, BookResponse> getBooks(Instant cursor, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/cursor")
                        .queryParam("cursor", cursor)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public BookSearchResponse searchBooks(BookSearchRequest request) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/search")
                        .queryParam("keyword", request.keyword())
                        .queryParam("page", request.pageable().getPageNumber())
                        .queryParam("size", request.pageable().getPageSize())
                        .queryParam("sort", request.pageable().getSort())
                        .build())
                .retrieve()
                .body(BookSearchResponse.class);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        return restClient.post()
                .uri("/users/auth/token/refresh")
                .body(Map.of("refreshToken", refreshToken))
                .retrieve()
                .body(RefreshTokenResponse.class);
    }

    @Override
    public HotKeywordResponse getHotKeywords(int size, LocalDate date) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hot-keywords")
                        .queryParam("size", size)
                        .queryParam("date", date.format(DateTimeFormatter.ISO_DATE))
                        .build())
                .retrieve()
                .body(HotKeywordResponse.class);
    }
}
