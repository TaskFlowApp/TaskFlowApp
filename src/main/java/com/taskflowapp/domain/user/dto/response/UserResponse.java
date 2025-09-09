package com.taskflowapp.domain.user.dto.response;

import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private UserRole role;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
    public UserResponse(
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
