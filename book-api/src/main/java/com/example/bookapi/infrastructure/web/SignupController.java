package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.application.in.SignupUseCase;
import com.example.bookapi.infrastructure.openapi.annotation.ApiErrorCodeExample;
import com.example.bookapi.infrastructure.openapi.annotation.ExceptionCodeExample;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Sign-up", description = "회원 가입 API")
public class SignupController {

    private final SignupUseCase signupUseCase;

    @ApiErrorCodeExample(examples = {
            @ExceptionCodeExample(title = "이미 사용중인 아이디일 때", code = "ID_DUPLICATED")
    })
    @PostMapping
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return signupUseCase.signup(request);
    }
}
