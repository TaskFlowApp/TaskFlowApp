package com.taskflowapp.domain.team.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.team.dto.TeamRequest;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // 팀 생성 //
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody TeamRequest teamRequest
    ) {
        TeamResponse createdTeam = teamService.createTeam(teamRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 성공적으로 생성되었습니다.", createdTeam));
    }

    // 팀 목록 조회 //
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> findAllTeams(
    ) {
        List<TeamResponse> teamResponseList = teamService.getAllTeams();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("팀 목록을 조회했습니다.", teamResponseList));
    }

    // 특정 팀 조회 //
    // 현재 프론트(팀관리 > 팀 탭 전환)에서 동작하는 게 아님
    // 추후 검색 기능 구현 시 다시 테스트 예정
    // DB 에서 정상 동작 확인 완료
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> findTeamById(
            @PathVariable Long teamId
    ) {
        TeamResponse teamResponse = teamService.getTeam(teamId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("팀 정보를 조회했습니다.",  teamResponse));
    }
}
