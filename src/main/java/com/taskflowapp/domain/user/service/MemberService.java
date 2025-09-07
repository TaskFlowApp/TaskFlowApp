package com.taskflowapp.domain.user.service;

import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.team.repository.TeamRepository;
import com.taskflowapp.domain.user.dto.request.MemberRequestDto;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
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
    public TeamResponse addMember(UserDetailsImpl userDetails, Long teamId, MemberRequestDto memberRequestDto) {
        User user = userRepository.findById(memberRequestDto.getUserId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.")
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.")
        );

        // 로그인한 유저가 관리자일 때 관리자는 모든 유저를 관리할 수 있지만 일반사용자는 본인의 상태(팀)만 변경할 수 있다.
        if (!Objects.equals(userDetails.getAuthUser().getRole(), UserRole.ADMIN)) {
            if (!Objects.equals(userDetails.getAuthUser().getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일반 사용자는 본인만 팀에 추가할 수 있습니다.");
            }
        }

        // 유저에 팀 아이디 추가
        user.changeTeam(team);            // 주인 쪽 설정
        team.getMembers().add(user);      // 양방향 동기화
        userRepository.save(user); // 주인만 저장하면 된다.

        // 응답 Dto 구성
        List<MemberResponseDto> members = team.getMembers().stream()
                .map(MemberResponseDto::from)
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

        // 팀에 속해 있지 않은 유저만 조회
        List<User> users = userRepository.findAllByTeamIsNull();

        List<MemberResponseDto> availableMembers = users.stream()
                .filter(user -> user.getTeam() == null || !Objects.equals(user.getTeam().getId(), teamId))
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
        return availableMembers;
    }

    // 팀 멤버 목록 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findTeamMember(Long teamId) {
        teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다")
        );

        // 팀 아이디를 기준으로 등록되어 있는 유저를 찾아서 출력
        List<User> users = userRepository.findAllByTeamId(teamId);

        List<MemberResponseDto> members = users.stream()
                .filter(user -> user.getTeam() != null)
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
        return members;
    }

    // 팀 멤버 제거
    public TeamResponse deleteMember(UserDetailsImpl userDetails, Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다")
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 팀 멤버가 아닙니다")
        );

        // 로그인한 유저가 관리자일 때 관리자는 모든 유저를 관리할 수 있지만 일반사용자는 본인의 상태(팀)만 변경할 수 있다.
        if (!Objects.equals(userDetails.getAuthUser().getRole(), UserRole.ADMIN)) {
            if (!Objects.equals(userDetails.getAuthUser().getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한입니다.");
            }
        }

        // 유저의 팀 아이디 제거
        team.getMembers().remove(user);
        user.changeTeam(null);
        userRepository.save(user); // 주인 쪽 저장

        List<MemberResponseDto> members = team.getMembers().stream()
                .map(MemberResponseDto::from)
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
