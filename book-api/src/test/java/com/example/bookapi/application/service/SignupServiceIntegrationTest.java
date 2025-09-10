package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.application.in.SignupUseCase;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import com.example.bookapi.infrastructure.aop.BookIndexingEventHandler;
import com.example.bookapi.infrastructure.cache.redis.TestRedisConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Import({TestRedisConfiguration.class})
@EnableAutoConfiguration(exclude = {
        ElasticsearchClientAutoConfiguration.class,
        ElasticsearchDataAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
})
public class SignupServiceIntegrationTest {
    @MockitoBean
    BookIndexingEventHandler bookIndexingEventHandler;

    @Autowired
    private SignupUseCase signupService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // Given
        SignupRequest request = new SignupRequest(
                "test",
                "password"
        );

        // When
        SignupResponse response = signupService.signup(request);

        // Then
        User saved = userRepository.findById(response.id()).orElseThrow();
        Assertions.assertThat(saved.getUsername()).isEqualTo(request.id());
        Assertions.assertThat(passwordEncoder.matches(request.password(), saved.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 아이디")
    void signup_fail_if_id_is_duplicated() {
        // Given
        SignupRequest request = new SignupRequest(
                "test",
                "password"
        );
        userRepository.save(new User(null, request.id(), passwordEncoder.encode(request.password()), "USER", AuditInfo.create(request.id())));

        // When, Then
        Assertions.assertThatThrownBy(() -> signupService.signup(request)).isInstanceOf(ApplicationException.class)
                .hasMessageContaining("중복된 아이디의 사용자가 존재합니다.");
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 아이디")
    void signup_fail_if_username_already_exists() {
        // Given
        SignupRequest request = new SignupRequest(
                "test",
                "password"
        );
        userRepository.save(User.create("test", "encodedPassword", "ROLE_USER"));


        // When & Then
        Assertions.assertThatThrownBy(() -> signupService.signup(request)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디가 null")
    void signup_fail_if_username_is_null() {
        // Given
        SignupRequest request = new SignupRequest(
                null,
                "password"
        );

        // When & Then
        Assertions.assertThatThrownBy(() -> signupService.signup(request)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호가 null")
    void signup_fail_if_password_is_null() {
        // Given
        SignupRequest request = new SignupRequest(
                "test",
                null
        );

        // When & Then
        Assertions.assertThatThrownBy(() -> signupService.signup(request)).isInstanceOf(RuntimeException.class);
    }
}
