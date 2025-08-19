package com.example.bookapi.infrastructure.security.config;

import com.example.bookapi.application.out.CacheProvider;
import com.example.bookapi.application.out.TokenProvider;
import com.example.bookapi.common.ErrorResponse;
import com.example.bookapi.infrastructure.security.filter.JwtFilter;
import com.example.bookapi.infrastructure.security.jwt.JjwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, TokenProvider jwtProvider, CacheProvider cacheProvider, AuthenticationEntryPoint entryPoint, AccessDeniedHandler accessDeniedHandler) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/users", "/api/users/auth/**").permitAll()
                                .requestMatchers("/api/books/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(new JwtFilter(jwtProvider, cacheProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    TokenProvider jwtTokenProvider(@Value("${secret}") String secret) {
        return new JjwtProvider(secret);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationEntryPoint entryPoint() {
        return (request, response, exception) -> {
            log.error(exception.getMessage());

            ErrorResponse errorResponse = new ErrorResponse("인증이 필요합니다.", "NOT_AUTHENTICATED", String.valueOf(System.currentTimeMillis()));
            String responseString = objectMapper.writeValueAsString(errorResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(responseString);
        };
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            log.error(exception.getMessage());

            ErrorResponse errorResponse = new ErrorResponse("접근 권한이 없습니다.", "ACCESS_DENIED", String.valueOf(System.currentTimeMillis()));
            String responseString = objectMapper.writeValueAsString(errorResponse);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(responseString);
        };
    }
}
