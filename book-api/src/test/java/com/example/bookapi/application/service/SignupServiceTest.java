package com.example.bookapi.application.service;


import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupService signupService;
    private final SignupRequest validRequest = new SignupRequest(
            "user123",
            "password123"
    );

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // Given
        String encodedPassword = "encodedPassword123";
        UUID userId = UUID.randomUUID();
        User savedUser = new User(userId, "user123", encodedPassword, "ROLE_USER", AuditInfo.create(userId.toString()));

        given(passwordEncoder.encode(validRequest.password())).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // When
        SignupResponse response = signupService.signup(validRequest);

        // Then
        verify(passwordEncoder).encode(validRequest.password());
        verify(userRepository).save(any(User.class));
        assertEquals(savedUser.getId(), response.id());
    }


    @Test
    @DisplayName("회원가입 시 비밀번호 인코딩이 된다.")
    void signup_passwordEncoding() {
        // Given
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(validRequest.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(encodedPassword, user.getPassword());
            return user;
        });

        // When
        signupService.signup(validRequest);

        // Then
        verify(passwordEncoder).encode(validRequest.password());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 아이디가 존재")
    void signup_duplicated_username() {
        // Given
        SignupRequest signupRequest = new SignupRequest("test", "password");
        when(userRepository.existsByUsername(signupRequest.id())).thenReturn(true);

        // When & Then

        Assertions.assertThatThrownBy(()-> signupService.signup(signupRequest)).isInstanceOf(ApplicationException.class)
                .hasMessageContaining("중복된 아이디의 사용자가 존재합니다.");
        verify(userRepository, times(1)).existsByUsername(signupRequest.id());
    }
}