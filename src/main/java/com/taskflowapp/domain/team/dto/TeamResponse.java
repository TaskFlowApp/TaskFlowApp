package com.taskflowapp.domain.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TeamResponse {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    // private List<UserResponse> members; 멤버 정보 추후 연결 예정
}
