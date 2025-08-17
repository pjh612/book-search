package com.example.bookapi.application.out;

import java.time.LocalDate;
import java.util.List;

public interface HotKeywordRepository {
    List<String> find(int size, LocalDate date);
}
