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
