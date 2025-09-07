package com.taskflowapp.domain.team.service;

import com.taskflowapp.domain.team.dto.DeleteTeamResponse;
import com.taskflowapp.domain.team.dto.TeamRequest;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.team.repository.TeamRepository;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // 팀 생성 //
    @Transactional
    public TeamResponse createTeam(TeamRequest teamRequest) {
        // 팀 중복 에러
        if (teamRepository.existsByName(teamRequest.getName())) {
            throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
        }

        Team team = Team.builder()
                .name(teamRequest.getName())
                .description(teamRequest.getDescription())
                .build();

        Team savedTeam = teamRepository.save(team);

        return TeamResponse.builder()
                .id(savedTeam.getId())
                .name(savedTeam.getName())
                .description(savedTeam.getDescription())
                .createdAt(savedTeam.getCreatedAt())
                .members(Collections.emptyList())
                // Collections.emptyList() : 변경 불가능한 빈 리스트 -> 명시적으로 '비어 있음' 보장
                .build();
    }

    // 팀 전체 목록 조회 //
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {

        // 1. 엔티티 저장 (DB 저장) //
        List<Team> teams = teamRepository.findAll();

        // 2. DTO 변환 (엔티티 리스트 -> DTO 리스트) //
        List<TeamResponse> teamResponses = teams.stream()
                .map(team -> TeamResponse.builder()
                        .id(team.getId())
                        .name(team.getName())
                        .description(team.getDescription())
                        .createdAt(team.getCreatedAt())
                        .members(
                                // Stream<User> 생성 //
                                team.getMembers().stream()
                                        // Stream<MemberResponseDto> 로 변환
                                        .map( member -> MemberResponseDto.builder()
                                                .id(member.getId())
                                                .username(member.getUsername())
                                                .name(member.getName())
                                                .email(member.getEmail())
                                                .role(member.getRole())
                                                .createdAt(member.getCreatedAt())
                                                .build()
                                        )
                                        // List<MemberResponseDto> 로 변환 //
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .collect(Collectors.toList());

        return teamResponses;
    }

    // 특정 팀 조회 //
    // 현재 프론트(팀관리 > 팀 탭 전환)에서 동작하는 게 아님
    // 추후 검색 기능 구현 시 다시 테스트 예정
    // DB 에서 정상 동작 확인 완료
    @Transactional(readOnly = true)
    public TeamResponse getTeam(Long teamId) {

        // 1. id 조회
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 팀이 없습니다.")
        );

        // 2. dto 변환
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(
                        team.getMembers().stream()
                                .map(member -> MemberResponseDto.builder()
                                        .id(member.getId())
                                        .username(member.getUsername())
                                        .name(member.getName())
                                        .email(member.getEmail())
                                        .role(member.getRole())
                                        .createdAt(member.getCreatedAt())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    // 팀 수정 //
    @Transactional
    public TeamResponse updateTeam(TeamRequest teamRequest, Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("팀을 찾을 수 없습니다.")
        );

        team.changeTeam(
                teamRequest.getName(),
                teamRequest.getDescription()
        );

        teamRepository.save(team);

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(
                        team.getMembers().stream()
                                .map(member -> MemberResponseDto.builder()
                                        .id(member.getId())
                                        .username(member.getUsername())
                                        .name(member.getName())
                                        .email(member.getEmail())
                                        .role(member.getRole())
                                        .createdAt(member.getCreatedAt())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    // 팀 삭제 //
    @Transactional
    public DeleteTeamResponse deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("팀을 찾을 수 없습니다.")
        );

        teamRepository.delete(team);

        return new DeleteTeamResponse("팀이 성공적으로 삭제되었습니다");
    }
}
