# TokenPayload

```java
package com.taskflowapp.domain.security;

import lombok.Getter;

import java.util.Date;

@Getter
public class TokenPayload {

    // Claim 필드
    // subject
    private String sub;
    // JWT ID. 토큰 자체의 고유 식별자(UUID 등)
    private String jti;
    // issued at: 토큰 발급 시간
    private Date iat;
    // 만료 시간 -> 토큰이 이 시간 이후로 무효
    private Date expiresAt;

    public TokenPayload(
            String sub,
            String jti,
            Date iat,
            Date expiresAt) {
        this.sub = sub;
        this.jti = jti;
        this.iat = iat;
        this.expiresAt = expiresAt;
    }
}
```

---

## 역할

- payload 정보 정의
- JWT를 만들 때 넣을 필드(Claims)를 하나의 객체로 묶어 전달 및 관리
- JwtProvider.createToken(TokenPayload)에 넣어 subject, id, issuedAt, expiration에 매핑
