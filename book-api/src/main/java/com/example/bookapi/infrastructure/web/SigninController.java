package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.RefreshTokenRequest;
import com.example.bookapi.application.dto.RefreshTokenResponse;
import com.example.bookapi.application.dto.SigninRequest;
import com.example.bookapi.application.dto.SigninResponse;
import com.example.bookapi.application.in.SigninUsecase;
import com.example.bookapi.infrastructure.openapi.annotation.ApiErrorCodeExample;
import com.example.bookapi.infrastructure.openapi.annotation.ExceptionCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auth")
@Tag(name = "Sign-in", description = "로그인/토큰 재발급 API")
public class SigninController {
    private final SigninUsecase signinUsecase;

    @PostMapping
    @Operation(
            summary = "로그인",
            description = "사용자 ID/비밀번호로 로그인하여 액세스/리프레시 토큰을 발급합니다."
    )
    @ApiErrorCodeExample(examples = {
            @ExceptionCodeExample(title = "아이디 또는 비밀번호가 일치하지 않을 때", code = "AUTHENTICATION_FAILED")
    })
    public SigninResponse signin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SigninRequest.class),
                            examples = {
                                    @ExampleObject(name = "예시",
                                            description = "기본 세팅된 테스트 계정으로 토큰을 획득할 수 있습니다. accessToken을 복사해서 Authorize 버튼을 눌러 Bearer Token에 붙여넣고 API를 호출해보세요. (access token 유효시간 15분)",
                                            value = "{\n  \"id\": \"test\",\n  \"password\": \"test\"\n}")
                            }
                    )
            )
            @Valid @RequestBody SigninRequest request
    ) {
        return signinUsecase.signin(request);
    }

    @PostMapping("/token/refresh")
    @Operation(
            summary = "토큰 재발급",
            description = "만료되었거나 만료 임박한 액세스 토큰을 리프레시 토큰으로 재발급합니다."
    )
    @ApiErrorCodeExample(examples = {
            @ExceptionCodeExample(title = "토큰이 유효하지 않을 때", code = "INVALID_TOKEN", description = "토큰이 만료되었거나 유효하지 않을 때")
    })
    public RefreshTokenResponse refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = {
                                    @ExampleObject(name = "예시",
                                            value = "{\n  \"refreshToken\": \"<jwt-refresh-token>\"\n}")
                            }
                    )
            )
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return signinUsecase.refreshToken(request.refreshToken());
    }
}
