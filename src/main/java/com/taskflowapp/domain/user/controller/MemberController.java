package com.taskflowapp.domain.user.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.team.dto.TeamResponse;
import com.taskflowapp.domain.user.dto.request.MemberRequestDto;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<ApiResponse<TeamResponse>> addMember(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long teamId,
            @RequestBody MemberRequestDto memberRequestDto
    ) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(ApiResponse.error("인증이 필요합니다"));
//        }
        TeamResponse teamResponse = memberService.addMember(teamId, memberRequestDto);

        return ResponseEntity.ok(ApiResponse.success("팀 멤버 추가 성공", teamResponse));
    }

    @GetMapping("/members") // task가 먼저 만들어져야만 이것을 테스트할 수 있다.. 그래서 아직 테스트하지 못함.
    public ResponseEntity<ApiResponse<List<MemberResponseDto>>> getMembers(
            @PathVariable Long teamId
    ) {
        List<MemberResponseDto> memberResponse = memberService.findTeamMember(teamId);

        return ResponseEntity.ok(ApiResponse.success("팀 멤버 목록을 조회했습니다.", memberResponse));
    }

    // 팀 멤버 제거
    @DeleteMapping("/members/{userId}")
    public ResponseEntity<ApiResponse<TeamResponse>> deleteMember(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        TeamResponse teamResponse = memberService.deleteMember(teamId, userId);
        return ResponseEntity.ok(ApiResponse.success("멤버가 성공적으로 제거되었습니다.", teamResponse));
    }
}
