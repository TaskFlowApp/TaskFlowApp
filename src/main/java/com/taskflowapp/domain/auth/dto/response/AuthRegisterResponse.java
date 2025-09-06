package com.taskflowapp.domain.auth.dto.response;

import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthRegisterResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRole role;    // DTO에 enum 타입 그대로
    private final LocalDateTime createdAt;

    public AuthRegisterResponse(
            Long id,
            String username,
            String email,
            String name,
            UserRole role,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }
}
