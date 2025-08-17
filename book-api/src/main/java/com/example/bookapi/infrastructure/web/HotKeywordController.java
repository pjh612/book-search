package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.application.in.QueryHotKeywordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hot-keywords")
public class HotKeywordController {
    private final QueryHotKeywordUseCase queryHotKeywordUseCase;

    @GetMapping
    public HotKeywordResponse getHotKeyword(@RequestParam int size, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date) {
        return queryHotKeywordUseCase.find(size, date);
    }

}
