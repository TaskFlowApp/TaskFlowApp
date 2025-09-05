package com.taskflowapp.domain.team.service;

import com.taskflowapp.domain.team.dto.TeamRequest;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

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
                .build();
    }
}
