package com.example.bookweb.controller;

import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.SigninRequest;
import com.example.bookweb.dto.SigninResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signin")
public class SigninController {
    private final BookApiClient client;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
        SigninResponse res = client.signin(request);
        if (res != null && res.refreshToken() != null && !res.refreshToken().isBlank()) {
            ResponseCookie cookie = ResponseCookie.from("refresh_token", res.refreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(res);
        }
        return ResponseEntity.ok(Map.of("accessToken", res.accessToken()));
    }

    @GetMapping
    public String signinPage() {
        return "signin";
    }
}
