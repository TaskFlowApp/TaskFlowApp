package com.taskflowapp.domain.team.dto;

import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class TeamResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<MemberResponseDto> members;

    // 정적 팩토리 메서드 //
    // 객체를 생성할 때 new 대신 클래스 내부의 static 메서드로 생성
    // 1. 중복 제거 가능 (builder로 매번 변환 필요 x)
    // 2. 가독성 향상
    // 3. 유연성
    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(
                        Optional.ofNullable(team.getMembers())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(member -> MemberResponseDto.from(member))
                                .collect(Collectors.toList())
                )
                .build();
    }
}