# SecurityConfig

```java
package com.taskflowapp.domain.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 스프링의 설정(Configuration) 파일로 등록 
// -> 스프링 컨테이너에 자동 등록. 단, 클래스 내부의 메서드가 자동 Bean 등록되지 않음
@Configuration
// Spring Security 기능 활성화
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    
    // 인증 매니저(AuthenticationManager) Bean 등록
    @Bean
    public AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // JWT 인증 필터(JwtAuthFilter) Bean 등록
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtProvider, userDetailsService);
    }

    // 비밀번호 암호화에 사용할 PasswordEncoder Bean 등록
    @Bean
    // BCrypt 알고리즘 사용
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Spring Security의 필터 체인 설정
    // CORS, CSRF 비활성화, 세션 관리 설정 등 JWT에 맞는 HTTP 보안 설정을 적용
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        // CORS 설정을 기본값으로 적용
        // -> WebConfig에서 설정한 내용 적용
        http.cors(Customizer.withDefaults())
                // CSRF 보호 기능 비활성화
                // -> JWT 인증 방식은 CSRF 공격에 상대적으로 안전하여 보통 비활성화 & 세션 기반 인증이 아니므로 필요 없음
                .csrf(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않고(STATELESS), 각 요청마다 JWT 인증 정보 검증
                // -> JWT 방식에 맞는 설정
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // "/auth/"로 시작하는 모든 요청은 인증 없이 허용
                        .requestMatchers("/auth/**").permitAll()
                        // 그 외 요청은 반드시 인증을 거쳐야만 접근 가능
                        .anyRequest().authenticated()    // "/auth/register", "/auth/login" -> 하면 안됨
                );    

        // Spring Security의 기본 필터(UsernamePasswordAuthenticationFilter)보다 
        // 먼저 커스텀 JWT 인증 필터인 jwtAuthFilter()를 실행하도록 순서 설정
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        
        // 최종 빌드
        // SecurityFilterChain 객체 생성 후 Spring Security에 등록
        return http.build();
    }
}
```

---

## 역할

- Spring Security의 모든 보안 규칙을 정의하는 가장 중요한 설정 파일

## AuthenticationManager

- Spring Security에서 로그인 인증을 처리하는 핵심 객체
- username 및 password 검증 등 인증 로직 수행

## `http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);`

1. 클라이언트 요청
2. Spring Security의 기본 필터(UsernamePasswordAuthenticationFilter)보다 커스텀 JWT 인증 필터인 JwtAuthFilter가 먼저 JWT 토큰 확인
3. 인증 처리 및 성공
4. Spring Security Context에 사용자 등록
    - Spring Security Context:
        - 현재 요청(request)과 연관된 인증 정보를 담는 저장소
        - 즉 현재 로그인한 사용자와 권한 정보를 저장하여 추후 인증 권한 체크 가능
        - 예:
            - `SecurityContextHolder.getContext().setAuthentication(authentication);`:
                - 현재 Security Context에 인증 정보를 넣는 역할
                - JWT 인증 필터에서 토큰 검증 후 로그인 사용자 정보 저장 시 사용
            - `SecurityContextHolder.getContext().getAuthentication()`:
                - 현재 Security Context에 들어있는 인증 정보를 가져올 때 사용
                - 이후 컨트롤러, 서비스, 권한 체크(@PreAuthorize)에서 활용


- 커스텀 JWT 인증 필터 사용 이유:
    - 폼 로그인:
        - Spring Security 기본 로그인 방식으로, 세션 기반 인증
        - 클라이언트가 ID 및 PW를 서버에 보내면 필터가 받아서 AuthenticationManager로 인증 처리
        - 로그인 성공하면 세션 생성 후 Spring Security Context에 사용자 등록
    - 현재 상황(문제):
        - JWT 인증은 이미 발급된 토큰을 사용
        - 클라이언트는 요청마다 토큰만 보내고 ID 및 PW는 안 보냄
        - Spring Security의 기본 필터는 ID 및 PW 없으면 동작 불가 -> JWT 검증 필요 => 커스텀 JWT 인증 필터 사용