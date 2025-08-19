package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.application.in.SignupUseCase;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.id())) {
            throw new IllegalArgumentException("중복된 아이디의 사용자가 존재합니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.create(request.id(), encodedPassword, "USER");
        User saved = userRepository.save(user);

        return new SignupResponse(saved.getId());
    }
}
