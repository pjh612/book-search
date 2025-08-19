package com.example.bookweb.controller;

import com.example.bookweb.client.BookApiClient;
import com.example.bookweb.dto.SignupRequest;
import com.example.bookweb.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {
    private final BookApiClient client;

    @PostMapping
    @ResponseBody
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return client.signup(request);
    }

    @GetMapping
    public String signupPage() {
        return "signup";
    }

}
