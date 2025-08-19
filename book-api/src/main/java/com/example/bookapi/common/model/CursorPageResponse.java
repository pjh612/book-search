package com.example.bookapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPageResponse<C, T> {
    private C cursor;
    private List<T> items;
}
