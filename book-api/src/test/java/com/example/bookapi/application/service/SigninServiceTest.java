package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.RefreshTokenResponse;
import com.example.bookapi.application.dto.SigninRequest;
import com.example.bookapi.application.dto.SigninResponse;
import com.example.bookapi.application.out.CacheProvider;
import com.example.bookapi.application.out.TokenProvider;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SigninServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private CacheProvider cacheProvider;
    private SigninService signinService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenProvider = mock(TokenProvider.class);
        cacheProvider = mock(CacheProvider.class);
        signinService = new SigninService(userRepository, passwordEncoder, tokenProvider, cacheProvider);
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "test", "encodedPassword", "ROLE_USER", AuditInfo.create(id.toString()));
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(tokenProvider.generateToken(anyString(), anyMap(), anyLong())).thenReturn("accessToken");
        when(tokenProvider.generateToken(anyString(), isNull(), anyLong())).thenReturn("refreshToken");

        SigninRequest request = new SigninRequest("test", "password");
        SigninResponse response = signinService.signin(request);

        assertThat(response.accessToken()).isEqualTo("accessToken");
        assertThat(response.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void signin_fail_wrong_password() {
        User user = User.create("test", "encodedPassword", "ROLE_USER");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        SigninRequest request = new SigninRequest("test", "wrong");

        assertThatThrownBy(() -> signinService.signin(request))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("아이디 비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void signin_fail_user_not_found() {
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        SigninRequest request = new SigninRequest("notfound", "password");

        assertThatThrownBy(() -> signinService.signin(request))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("아이디 비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_success() {
        String accessToken = "access";
        String refreshToken = "refresh";
        String newAccessToken = "newAccess";
        String newRefreshToken = "newRefresh";

        when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(cacheProvider.hasKey(anyString())).thenReturn(false);
        when(cacheProvider.get(refreshToken, String.class)).thenReturn(Optional.of(accessToken));
        doNothing().when(cacheProvider).save(eq(newRefreshToken), eq(newAccessToken), any(Duration.class));
        when(tokenProvider.renewToken(eq(accessToken), any(Long.class))).thenReturn(newAccessToken);
        when(tokenProvider.renewToken(eq(refreshToken), any(Long.class))).thenReturn(newRefreshToken);

        RefreshTokenResponse response = signinService.refreshToken(refreshToken);

        assertThat(response.accessToken()).isEqualTo(newAccessToken);
        assertThat(response.refreshToken()).isEqualTo(newRefreshToken);

        verify(tokenProvider).renewToken(eq(accessToken), any(Long.class));
        verify(tokenProvider).renewToken(eq(refreshToken), any(Long.class));
        verify(cacheProvider).save(eq(newRefreshToken), eq(newAccessToken), any(Duration.class));
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 토큰이 유효하지 않음")
    void refreshToken_fail_invalid_token() {
        when(tokenProvider.validateToken("badToken")).thenReturn(false);

        assertThatThrownBy(() -> signinService.refreshToken("badToken"))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("유효하지 않은 토큰입니다.");
    }
}