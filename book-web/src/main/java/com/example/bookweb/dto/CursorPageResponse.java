package com.example.bookweb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CursorPageResponse<ID, T> {
    private ID cursor;
    private List<T> items;

    public CursorPageResponse(ID cursor, List<T> items) {
        this.cursor = cursor;
        this.items = items;
    }
}
