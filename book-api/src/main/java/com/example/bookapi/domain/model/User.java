package com.example.bookapi.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
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
        if(username==null || username.isBlank()) {
            throw new IllegalArgumentException("아이디를 입력 해주세요.");
        }
        if(password==null || password.isBlank()) {
            throw new IllegalArgumentException("패스워드를 입력 해주세요.");
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
