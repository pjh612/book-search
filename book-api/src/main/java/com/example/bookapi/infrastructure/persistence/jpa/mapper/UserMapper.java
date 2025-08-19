package com.example.bookapi.infrastructure.persistence.jpa.mapper;

import com.example.bookapi.domain.model.AuditInfo;
import com.example.bookapi.domain.model.User;
import com.example.bookapi.infrastructure.persistence.jpa.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getAuditInfo() == null ? null : user.getAuditInfo().getCreatedAt(),
                user.getAuditInfo() == null ? null : user.getAuditInfo().getUpdatedAt(),
                user.getAuditInfo() == null ? null : user.getAuditInfo().getCreatedBy(),
                user.getAuditInfo() == null ? null : user.getAuditInfo().getUpdatedBy()
        );
    }

    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                new AuditInfo(
                        userEntity.getCreatedAt(),
                        userEntity.getCreatedBy(),
                        userEntity.getUpdatedAt(),
                        userEntity.getUpdatedBy()
                )
        );
    }
}
