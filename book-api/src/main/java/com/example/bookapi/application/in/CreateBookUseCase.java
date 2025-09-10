package com.example.bookapi.application.in;

import com.example.bookapi.application.dto.CreateBookRequest;

import java.util.UUID;

public interface CreateBookUseCase {
    UUID create(CreateBookRequest request);
}
