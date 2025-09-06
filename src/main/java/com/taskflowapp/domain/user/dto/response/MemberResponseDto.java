package com.taskflowapp.domain.user.dto.response;

import com.taskflowapp.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private final Long id;
    private final String username; // 로그인 아이디
    private final String name; // 유저명
    private final String email;
    private UserRole role;
    private final LocalDateTime createdAt;
}
