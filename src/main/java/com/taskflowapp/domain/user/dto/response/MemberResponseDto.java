package com.taskflowapp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    // 정적 팩토리 메서드
    public static MemberResponseDto from(User user) {
        return MemberResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 정적 팩토리 메서드(생성일 제외한 정보)
    public static MemberResponseDto fromWithoutCreatedAt(User user) {
        return MemberResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
