package com.example.bookapi.application.in;

import com.example.bookapi.application.dto.HotKeywordResponse;

import java.time.LocalDate;

public interface QueryHotKeywordUseCase {
    HotKeywordResponse find(int size, LocalDate localDate);
}
