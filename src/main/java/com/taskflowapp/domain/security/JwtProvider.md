# JwtProvider

```java
package com.taskflowapp.domain.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

// 로그 객체(log) 자동 생성
// log.info(), log.error() 같은 메서드 사용 가능 
@Slf4j
// Spring Bean 등록 
// -> 다른 클래스에서 @Autowired 또는 생성자 주입으로 JwtProvider 사용 가능
@Component
public class JwtProvider {

    // HTTP 요청 헤더에서 JWT를 읽을 때 사용하는 키(Authorization) 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // JWT 토큰 접두사(표준 양식): "Bearer "
    // -> 뒤에 오는 문자열이 토큰이라는 걸 알려줌
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료 시간: 60분(참고: 1000L = 1000밀리초 = 1초)
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    // application.yml 파일에 있는 "jwt.secret.key" 값 주입받기
    // ${} 안의 값 = yml의 키 이름
    @Value("${jwt.secret.key}")
    // application.yml의 Base64 인코딩된 비밀 키 문자열
    private String secretKey;
    // 토큰 서명(Signature) 생성 및 검증 시 사용하는 키 객체
    // Base64 디코딩한 secretKey로 생성
    private SecretKey key;
    
    // 기본 생성자(생략)
    // 생성자 시점에서는 @Value로 주입될 secretKey 값이 아직 없을 수 있음 -> 바로 초기화시 오류 발생 가능
    public JwtProvider() {
    }

    // Bean 생성 후, 의존성 주입(@Value, @Autowired 등) 완료 시 자동으로 실행되는 초기화 메서드
    @PostConstruct
    public void init() {
        // Base64로 인코딩된 secretKey -> 디코딩 -> SecretKey 객체로 변환
        // => JWT 생성 및 검증 시 사용
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // 디코딩된 바이트 배열을 기반으로 HMAC-SHA 키 생성
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성 전, 토큰에 담을 정보(payload) 객체 생성 메서드
    public TokenPayload createTokenPayload(String username) {
        Date date = new Date();
        return new TokenPayload(
                // subject 즉 사용자 식별자(여기서는 username)
                username,
                // 토큰 고유 ID, UUID 즉 고유 식별자(jti)
                UUID.randomUUID().toString(),
                // 토큰 발급 시간(iat)
                date,
                // 만료 시간(expiresAt, iat + 1시간)
                new Date(date.getTime() + TOKEN_TIME)
        );
    }

    // payload를 바탕으로 JWT 토큰 문자열 생성
    public String createToken(TokenPayload payload) {
        return Jwts.builder()
                // 토큰의 주체 즉 로그인한 사용자 식별자(아이디) 설정
                .subject(payload.getSub())
                // 토큰 만료 시간 설정
                .expiration(payload.getExpiresAt())
                // 토큰 발급 시간 설정
                .issuedAt(payload.getIat())
                // 토큰 자체를 구분하는 토큰 고유 식별자 지정
                // -> 재발급, 블랙리스트, 중복 방지 용도
                .id(payload.getJti())
                // HMAC-SHA256 알고리즘과 비밀 키로 서명
                .signWith(key, Jwts.SIG.HS256)
                // 위 모든 정보를 취합해 최종 토큰 문자열 생성
                .compact();
    }

    // HTTP 요청 헤더에서 토큰 추출
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // 토큰 접두사 유무 확인 후, 있다면 제거하여 순수한 토큰 문자열 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());    // 상수
        }

        return null;
    }

    // 토큰의 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 및 서명 검증
            // 서명이 틀렸거나 만료 또는 변조 시 예외 발생
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 유효하지 않을시 로그 남김
            log.error("Invalid Token: {}", e.getMessage());

        }

        return false;

    }

    // 토큰에서 사용자 정보(여기서는 Claims) 추출
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
```

---

## 역할

- JWT(Json Web Token)의 생성, 검증, payload(Claims) 추출을 담당하는 핵심 클래스
    - 생성: 로그인 시 사용자의 아이디(username)을 바탕으로 createToken 메서드 호출 및 토큰 생성
    - 추출: getJwtFromHeader 메서드로 HTTP 요청 헤더에서 토큰 꺼냄
    - 검증: validateToken 메서드로 토큰이 만료 및 변조 여부 확인

## JWT 기본 구조

1. Header:
    - 토큰 타입과 서명 알고리즘 정보
        - 예: { "alg": "HS256", "typ": "JWT" }
2. Payload(Claims) :
    - 토큰 안에 담고 싶은 정보
    - Claim: payload에 넣는 정보 항목
        - 예: claims.get("email") -> 토큰 안에 있던 이메일 가져오기
    - sub: 주체(subject). 보통 id나 username
        - 예: { "sub": "123", "email": "user@test.com", "userRole": "ADMIN" }
3. Signature(서명): 위 두 부분을 합쳐 비밀키로 암호화한 값. 토큰 위조 방지용

## HMAC-SHA(Hash-based Message Authentication Code)

- 메시지(Header + Payload)가 위변조 여부와 발신자 검증에 사용
- 메시지와 비밀 키를 해시 함수에 넣어 계산하여 토큰 서명(Signature) 생성 -> 서버에서 토큰 위조 여부 검증 가능
- 기본적으로 HS256 알고리즘을 사용

## `UUID.randomUUID().toString()`
- `UUID`(Universally Unique Identifier): 
  - 중복되지 않는 고유 식별자
  - 길이는 128비트(16바이트)
  - 보통 16진수와 하이픈(-) 조합으로 표시
- `randomUUID()`:
  - 임의로 고유한 UUID 생성
  - 중복될 가능성이 없으므로 토큰 ID(jti) 등 고유 값이 필요할 때 안전하게 사용 가능
- `toString()`: UUID 객체를 문자열 형태로 변환