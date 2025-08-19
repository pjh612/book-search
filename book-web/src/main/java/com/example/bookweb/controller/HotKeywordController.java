package com.example.bookweb.controller;

import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.HotKeywordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hot-keywords")
public class HotKeywordController {

    private final BookApiClient client;

    @GetMapping
    public HotKeywordResponse getHotKeyword() {
        return client.getHotKeywords(10, LocalDate.now());
    }
}
