package com.taskflowapp.domain.user.service;

import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.team.repository.TeamRepository;
import com.taskflowapp.domain.user.dto.request.MemberRequestDto;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    // 팀 멤버 추가!
    public TeamResponse addMember(Long teamId, MemberRequestDto memberRequestDto) {
        User user = userRepository.findById(memberRequestDto.getUserId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.")
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.")
        );

        // 이미 해당 팀에 멤버(유저)가 추가되어 있다면 (멤버 중복 방지)
        if (user.getTeam() != null && Objects.equals(user.getTeam().getId(), team.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
        }

        // 연관관계 설정
        user.changeTeam(team);            // 주인 쪽 설정
        team.getMembers().add(user);   // 양방향 동기화
        userRepository.save(user); // 주인만 저장하면 된다.

        // 응답 Dto 구성
        List<MemberResponseDto> members = team.getMembers().stream()
                .map(selectUser -> MemberResponseDto.builder()
                        .id(selectUser.getId())
                        .username(selectUser.getUsername())
                        .name(selectUser.getName())
                        .email(selectUser.getEmail())
                        .role(selectUser.getRole())
                        .createdAt(selectUser.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(members)
                .build();
    }

    // 추가 가능한 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllAvailableUsers(Long teamId) {
        teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다")
        );
        List<User> users = userRepository.findAll();
        List<MemberResponseDto> availableMembers = users.stream()
                .filter(user -> user.getTeam() == null || !Objects.equals(user.getTeam().getId(), teamId))
                .map(user -> MemberResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return availableMembers;
    }

    // 팀 멤버 목록 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findTeamMember(Long teamId) {
        teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다")
        );
        List<User> users = userRepository.findAll();
        List<MemberResponseDto> members = users.stream()
                .filter(user -> user.getTeam() != null)
                .map(user -> MemberResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return members;
    }

    // 팀 멤버 제거
    public TeamResponse deleteMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 팀 멤버가 아닙니다")
        );
        if (user.getTeam() == null || !Objects.equals(user.getTeam().getId(), team.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
        }

        // 연관관계 제거
        team.getMembers().remove(user);
        user.changeTeam(null);
        userRepository.save(user); // 주인 쪽 저장

        List<MemberResponseDto> members = team.getMembers().stream()
                .map(selectUser -> MemberResponseDto.builder()
                        .id(selectUser.getId())
                        .username(selectUser.getUsername())
                        .name(selectUser.getName())
                        .email(selectUser.getEmail())
                        .role(selectUser.getRole())
                        .createdAt(selectUser.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(members)
                .build();
    }
}
