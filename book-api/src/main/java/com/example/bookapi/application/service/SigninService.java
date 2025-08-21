package com.example.bookapi.application.service;


import com.example.bookapi.application.dto.RefreshTokenResponse;
import com.example.bookapi.application.dto.SigninRequest;
import com.example.bookapi.application.dto.SigninResponse;
import com.example.bookapi.application.in.SigninUsecase;
import com.example.bookapi.application.out.CacheProvider;
import com.example.bookapi.application.out.TokenProvider;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

import static com.example.bookapi.common.exception.ExceptionCode.AUTHENTICATION_FAILED;
import static com.example.bookapi.common.exception.ExceptionCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
public class SigninService implements SigninUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CacheProvider cacheProvider;

    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 15 * 60;
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 2 * 60 * 60;

    @Override
    public SigninResponse signin(SigninRequest request) {
        User user = userRepository.findByUsername(request.id())
                .orElseThrow(() -> new ApplicationException(AUTHENTICATION_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApplicationException(AUTHENTICATION_FAILED);
        }


        Map<String, Object> claims = Map.of("username", user.getUsername(),
                "role", user.getRole());
        String accessToken = tokenProvider.generateToken(user.getId().toString(), claims, ACCESS_TOKEN_EXPIRATION_SECONDS * 1000);
        String refreshToken = tokenProvider.generateToken(user.getId().toString(), null, REFRESH_TOKEN_EXPIRATION_SECONDS * 1000);

        cacheProvider.save(refreshToken, accessToken, Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS));

        return new SigninResponse(accessToken, refreshToken);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken) || isBlocked(refreshToken)) {
            throw new ApplicationException(INVALID_TOKEN);
        }
        String accessToken = cacheProvider.get(refreshToken, String.class)
                .orElseThrow(() -> new ApplicationException(INVALID_TOKEN));

        String newAccessToken = tokenProvider.renewToken(accessToken, ACCESS_TOKEN_EXPIRATION_SECONDS * 1000);
        String newRefreshToken = tokenProvider.renewToken(refreshToken, REFRESH_TOKEN_EXPIRATION_SECONDS * 1000);

        cacheProvider.save(String.format("blocked:token:%s", refreshToken), null, Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS));
        cacheProvider.save(newRefreshToken, newAccessToken, Duration.ofSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS));

        return new RefreshTokenResponse(newAccessToken, newRefreshToken);
    }

    private boolean isBlocked(String refreshToken) {
        return cacheProvider.hasKey(String.format("blocked:token:%s", refreshToken));
    }
}
