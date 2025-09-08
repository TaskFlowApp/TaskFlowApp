package com.taskflowapp.domain.team.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.team.dto.DeleteTeamResponse;
import com.taskflowapp.domain.team.dto.TeamRequest;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // 팀 생성 //
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody TeamRequest teamRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 전역 예외 클래스 생성 후 성공 응답 외 삭제 예정
        try {
            TeamResponse createdTeam = teamService.createTeam(teamRequest, userDetails);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("팀이 성공적으로 생성되었습니다.", createdTeam));

        // 팀 중복 에러 처리 //
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));

        // 관리자 아닐 경우 에러 처리 //
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
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
        // 전역 예외 클래스 생성 후 성공 응답 외 삭제 예정
        // UI상 예외 발생 X -> 보안상 구현
        try {
            TeamResponse teamResponse = teamService.getTeam(teamId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("팀 정보를 조회했습니다.",  teamResponse));

        } catch (IllegalArgumentException e) {
            // 팀 없음 에러 처리 //
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 팀 수정 //
    @PutMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @Valid @RequestBody TeamRequest teamRequest,
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 전역 예외 클래스 생성 후 성공 응답 외 삭제 예정
        try {
            TeamResponse teamUpdateResponse = teamService.updateTeam(teamRequest, teamId, userDetails);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("팀 정보가 성공적으로 업데이트되었습니다.", teamUpdateResponse));

        // 팀 없음 에러 처리 // UI상 예외 발생 X -> 보안상 구현
        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        // 관리자 아닐 경우 에러 처리 //
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }


    }

    // 팀 삭제 //
    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<DeleteTeamResponse>> deleteTeam(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 전역 예외 클래스 생성 후 성공 응답 외 삭제 예정
        try {
            DeleteTeamResponse deleteTeamResponse = teamService.deleteTeam(teamId, userDetails);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("팀이 성공적으로 삭제되었습니다.", deleteTeamResponse));

        // 팀 없음 에러 처리 // UI상 예외 발생 X -> 보안상 구현
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));

        // 관리자 아닐 경우 에러 처리 //
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }


    }
}
