package com.taskflowapp.domain.auth.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.auth.dto.request.AuthLoginRequest;
import com.taskflowapp.domain.auth.dto.request.AuthRegisterRequest;
import com.taskflowapp.domain.auth.dto.request.AuthWithdrawRequest;
import com.taskflowapp.domain.auth.dto.response.AuthLoginResponse;
import com.taskflowapp.domain.auth.dto.response.AuthRegisterResponse;
import com.taskflowapp.domain.auth.service.AuthService;
import com.taskflowapp.domain.security.authuser.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthRegisterResponse>> register(
            @Valid @RequestBody AuthRegisterRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("회원가입이 완료되었습니다.", authService.register(request))
        );
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponse>> login(
            @Valid @RequestBody AuthLoginRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("로그인이 완료되었습니다.", authService.login(request))
        );
    }

    // 회원탈퇴
    // 명세서상 HTTP 메서드가 "POST"
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(
            @Valid @RequestBody AuthWithdrawRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        authService.withdraw(userDetails.getUsername(), request);

        return ResponseEntity.ok(
                ApiResponse.success("회원탈퇴가 완료되었습니다.")
        );
    }
}
