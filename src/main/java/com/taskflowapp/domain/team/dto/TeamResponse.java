package com.taskflowapp.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    // private List<UserResponse> members; 멤버 정보 추후 연결 예정
}
