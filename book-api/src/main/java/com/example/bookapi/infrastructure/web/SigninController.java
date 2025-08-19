package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.RefreshTokenRequest;
import com.example.bookapi.application.dto.RefreshTokenResponse;
import com.example.bookapi.application.dto.SigninRequest;
import com.example.bookapi.application.dto.SigninResponse;
import com.example.bookapi.application.in.SigninUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
public class SigninController {
    private final SigninUsecase signinUsecase;

    @PostMapping
    public SigninResponse signin(@RequestBody SigninRequest request) {
        return signinUsecase.signin(request);
    }

    @PostMapping("/token/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return signinUsecase.refreshToken(request.refreshToken());
    }
}
