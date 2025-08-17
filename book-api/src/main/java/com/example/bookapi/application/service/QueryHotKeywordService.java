package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.application.in.QueryHotKeywordUseCase;
import com.example.bookapi.application.out.HotKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryHotKeywordService implements QueryHotKeywordUseCase {
    private final HotKeywordRepository hotKeywordRepository;

    @Override
    public HotKeywordResponse find(int size, LocalDate date) {
        List<String> hotKeywordResponse = hotKeywordRepository.find(size, date);

        return new HotKeywordResponse(hotKeywordResponse);
    }
}
