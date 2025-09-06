package com.taskflowapp.domain.user.controller;

import com.taskflowapp.common.ApiResponse;
import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.user.dto.response.UserResponse;
import com.taskflowapp.domain.user.repository.UserRepository;
import com.taskflowapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

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
}
