package com.taskflowapp.domain.team.dto;

import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TeamResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<MemberResponseDto> members;
}
