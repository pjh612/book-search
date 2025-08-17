package com.example.keywordconsumerservice.application.service;

import com.example.keywordconsumerservice.application.in.RegisterHotKeywordUseCase;
import com.example.keywordconsumerservice.application.out.HotKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterHotKeywordService implements RegisterHotKeywordUseCase {
    private final HotKeywordRepository hotKeywordRepositoryPort;

    @Override
    public void registerKeyword(String keyword) {
        if(keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        hotKeywordRepositoryPort.register(keyword.trim());
    }
}
