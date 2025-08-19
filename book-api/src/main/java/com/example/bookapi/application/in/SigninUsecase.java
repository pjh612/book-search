package com.example.bookapi.application.in;


import com.example.bookapi.application.dto.RefreshTokenResponse;
import com.example.bookapi.application.dto.SigninRequest;
import com.example.bookapi.application.dto.SigninResponse;

public interface SigninUsecase {
    SigninResponse signin(SigninRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);
}
