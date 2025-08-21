package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.SignupRequest;
import com.example.bookapi.application.dto.SignupResponse;
import com.example.bookapi.application.in.SignupUseCase;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bookapi.common.exception.ExceptionCode.ID_DUPLICATED;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.id())) {
            throw new ApplicationException(ID_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.create(request.id(), encodedPassword, "USER");
        User saved = userRepository.save(user);

        return new SignupResponse(saved.getId());
    }
}
