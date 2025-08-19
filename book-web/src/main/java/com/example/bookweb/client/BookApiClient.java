package com.example.bookweb.client;

import com.example.bookweb.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface BookApiClient {
    SignupResponse signup(SignupRequest signupRequest);

    SigninResponse signin(SigninRequest signinRequest);

    BookDetailResponse getBookDetailById(UUID id);

    Page<BookResponse> getBooks(Pageable pageable);

    CursorPageResponse<Instant, BookResponse> getBooks(Instant cursor, int size);

    BookSearchResponse searchBooks(BookSearchRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);

    HotKeywordResponse getHotKeywords(int size, LocalDate date);
}
