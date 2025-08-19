package com.example.bookapi.application.in;


import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;

public interface SignupUseCase {
    SignupResponse signup(SignupRequest request);
}
