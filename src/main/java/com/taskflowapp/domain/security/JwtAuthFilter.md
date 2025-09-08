# JwtAuthFilter

```java
package com.taskflowapp.domain.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
// 요청당 한 번만 실행되는 OncePerRequestFilter 상속받음
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    // 필터링 로직 구현 메서드
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 화이트리스트(공개경로): 특정 경로(로그인, 회원가입)는 인증 과정 건너뛰기
        if (shouldSkipAuthentication(request)) {
            // 다음 필터로 요청 넘김
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰 가져오기
        String accessToken = jwtProvider.getJwtFromHeader(request);
        // 토큰 존재 시
        if (StringUtils.hasText(accessToken)) {
            // 토큰 유효성 검증
            if (!jwtProvider.validateToken(accessToken)) {
                // 토큰 유효하지 않으면 에러 로그 남김
                log.error("AccessToken Error");
                // 여기서 처리 중단
                return;
            }

            // 유효한 토큰이면 사용자 정보 추출
            Claims info = jwtProvider.getUserInfoFromToken(accessToken);

            try {
                // 추출한 username으로 인증 정보 설정
                setAuthentication(info.getSubject());
            } 
            // 인증 설정 중 에러 발생 시 중단 
            catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 모든 처리 완료 시 다음 필터로 요청 넘김
        filterChain.doFilter(request, response);
    }

    // SecurityContextHolder에 인증 객체 설정 메서드
    public void setAuthentication(String username) {
        // 빈 Spring Security Context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // Authentication 객체 생성
        Authentication authentication = createAuthentication(username);
        // context에 인증 객체 넣기
        context.setAuthentication(authentication);
        
        // Spring Security의 전역 공간에 context 저장
        SecurityContextHolder.setContext(context);
    }

    // UsernamePasswordAuthenticationToken 생성 메서드
    private Authentication createAuthentication(String username) {
        // DB에서 사용자 정보 가져오기
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // principal, userDetails, authorities를 포함한 인증 토큰 생성
        return new UsernamePasswordAuthenticationToken(
                // principal = UserDetails = 로그인한 사용자 정보
                userDetails,
                // credentials = 자격증명(JWT 방식에서는 비밀번호 검증 불필요하므로 null)
                null,
                // authorities = 권한(역할)
                userDetails.getAuthorities());
    }

    // 인증을 건너뛸 경로인지 확인하는 메서드
    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        // 요청의 URL(컨텍스트 제외한 경로)를 문자열로 얻음
        // 컨텍스트 경로(context path): 애플리케이션이 배포된 기본 경로
        String path = request.getRequestURI();

        // 화이트리스트 목록을 배열로 정의 -> 해당 경로는 JWT 검사 건너뜀
        String[] publicPaths = {
                "/api/auth/register",
                "/api/auth/login"
        };

        // 반복문 돌면서 요청 URL이 공개 경로로 시작하는지 확인
        for (String publicPath : publicPaths) {
            // 요청 경로가 publicPath로 시작하면 공개 경로
            if (path.startsWith(publicPath)) {
                // 필터가 인증을 건너뛴 경우 디버그 로그 남김
                log.debug("Skipping JWT authentication for public path: {}", path);
                return true;
            }
        }

        return false;
    }
}
```

---

## 역할

- 모든 HTTP 요청에 대해 JWT 토큰의 유효성을 검증
- 유효한 요청이라면 Spring Security ContextHolder에 인증 정보를 넣어주는 핵심 필터
- 위 필터가 성공적으로 작동해야만 Spring Security가 해당 요청을 인증된 요청으로 인식 -> UserController의 메서드들이 정상적으로 실행

# UsernamePasswordAuthenticationToken
- Authentication 구현체 중 하나로, 주로 사용자 인증 정보를 담는 용도

```java
// 인증 전(미인증) 용도: 주로 인증을 시도할 때 사용
new UsernamePasswordAuthenticationToken(principal, credentials);

// 인증 완료(인증된 토큰) 용도: 권한(authorities)까지 포함하면 authenticated = true
new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
```