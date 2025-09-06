package com.taskflowapp.domain.auth.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.auth.dto.request.AuthLoginRequest;
import com.taskflowapp.domain.auth.dto.request.AuthRegisterRequest;
import com.taskflowapp.domain.auth.dto.response.AuthLoginResponse;
import com.taskflowapp.domain.auth.dto.response.AuthRegisterResponse;
import com.taskflowapp.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthRegisterResponse>> register(
            @RequestBody AuthRegisterRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("회원가입이 완료되었습니다.", authService.register(request))
        );
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponse>> login(
            @RequestBody AuthLoginRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("로그인이 완료되었습니다.", authService.login(request))
        );
    }
}
