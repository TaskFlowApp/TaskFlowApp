package com.taskflowapp.domain.auth.service;

import com.taskflowapp.domain.auth.dto.request.AuthLoginRequest;
import com.taskflowapp.domain.auth.dto.request.AuthRegisterRequest;
import com.taskflowapp.domain.auth.dto.response.AuthLoginResponse;
import com.taskflowapp.domain.auth.dto.response.AuthRegisterResponse;
import com.taskflowapp.domain.security.JwtProvider;
import com.taskflowapp.domain.security.TokenPayload;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public AuthRegisterResponse register(AuthRegisterRequest request) {
        // 유저네임 중복 여부 확인 -> 명세서에 있음
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");    // 명세서상 메세지 내용: "이미 존재하는 사용자명입니다.". Conflict라고 하는게 더 정확하나 명세서대로 진행
        }

        // 이메일 중복 여부 확인 -> 명세서에 없음
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Role 결정
        UserRole role = UserRole.fromRegistration(
                request.getUsername(),
                request.getEmail()
        );

        // 유저 생성
        User savedUser = new User(
                request.getEmail(),
                request.getUsername(),
                request.getName(),
                encodedPassword,
                role
        );

        userRepository.save(savedUser);
        return new AuthRegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole(),
                savedUser.getCreatedAt());
    }

    // 로그인
    @Transactional(readOnly = true)
    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다.")    // 명세서상 메세지 내용: "잘못된 사용자명 또는 비밀번호입니다."
        );
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다.");
        }
        TokenPayload payload = jwtProvider.createTokenPayload(user.getUsername());
        String accessToken = jwtProvider.createToken(payload);

        return new AuthLoginResponse(accessToken);
    }

    // 로그아웃

    // 회원탈퇴
}
