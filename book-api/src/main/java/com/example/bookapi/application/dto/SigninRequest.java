package com.example.bookapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SigninRequest(
        @NotBlank
        @Size(min = 4, max = 20)
        String id,
        String password
) {
}
