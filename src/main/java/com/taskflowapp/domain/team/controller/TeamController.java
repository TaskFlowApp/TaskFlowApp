package com.taskflowapp.domain.team.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.team.dto.TeamRequest;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // 팀 생성 //
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody TeamRequest teamRequest
    ) {
        TeamResponse result = teamService.createTeam(teamRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 성공적으로 생성되었습니다.", result));
    }
}
