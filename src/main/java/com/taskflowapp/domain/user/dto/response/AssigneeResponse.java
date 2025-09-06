package com.taskflowapp.domain.user.dto.response;

import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class AssigneeResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final UserRole role;

    public AssigneeResponse(
            Long id,
            String email,
            String name,
            UserRole role
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
