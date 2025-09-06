package com.taskflowapp.domain.user.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.dto.response.UserResponse;
import com.taskflowapp.domain.user.repository.UserRepository;
import com.taskflowapp.domain.user.service.MemberService;
import com.taskflowapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MemberService memberService;

    // 현재 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> findUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // userDetails에서 username 꺼내서 서비스 호출
        UserResponse userResponse = userService.findUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("사용자 정보를 조회했습니다.", userResponse)
        );
    }

    // 추가 가능한 사용자 목록 조회
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<MemberResponseDto>>> findAvailableUsers(
            @RequestParam Long teamId
    ) {
        return ResponseEntity.ok(ApiResponse.success("사용 가능한 사용자 목록을 조회했습니다.", memberService.findAllAvailableUsers(teamId)));
    }
}
