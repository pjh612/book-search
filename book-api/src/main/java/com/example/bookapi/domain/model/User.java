package com.example.bookapi.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private UUID id;
    private String username;
    private String password;
    private String role;
    private AuditInfo auditInfo;

    public User(UUID id, String username, String password, String role, AuditInfo auditInfo) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("아이디를 입력 해주세요.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("패스워드를 입력 해주세요.");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("권한을 입력 해주세요.");
        }
        if (username.length() < 4 || username.length() > 20) {
            throw new IllegalArgumentException("아이디는 4자 이상 20자 이하로 입력 해주세요.");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.auditInfo = auditInfo;
    }

    public static User create(String id, String encodedPassword, String role) {
        return new User(
                null,
                id,
                encodedPassword,
                role,
                AuditInfo.create(id)
        );
    }
}
