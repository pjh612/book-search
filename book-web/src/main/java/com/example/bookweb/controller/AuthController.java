package com.example.bookweb.controller;

import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final BookApiClient client;

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<Map<String, String>> refreshToken(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
        RefreshTokenResponse tokens = client.refreshToken(refreshToken);
        if (tokens == null || tokens.accessToken() == null || tokens.accessToken().isBlank()) {
            return ResponseEntity.status(401).build();
        }

        if (tokens.refreshToken() != null && !tokens.refreshToken().isBlank()) {
            ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.refreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok(Map.of("accessToken", tokens.accessToken()));
    }
}
