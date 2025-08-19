package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.application.in.SignupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SignupController {

    private final SignupUseCase signupUseCase;

    @PostMapping
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return signupUseCase.signup(request);
    }
}
