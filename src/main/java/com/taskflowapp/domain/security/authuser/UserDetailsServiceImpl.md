# UserDetailsServiceImpl

```java
package com.taskflowapp.domain.security;

import com.taskflowapp.domain.security.authuser.UserDetailsImpl;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// UserDetailsService 인터페이스를 구현하여 사용자 정보를 불러오는 역할
// UserDetailsService: Spring Security에서 인증 시 사용자 정보를 가져오는 핵심 인터페이스
public class UserDetailsServiceImpl implements UserDetailsService {

    // 사용자 정보를 DB에서 가져오기 위해 UserRepository 주입
    private final UserRepository userRepository;
    
    /*
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    */

    @Override
    // Spring Security가 loadUserByUsername(String username) 호출해서 사용자를 찾고, 찾은 사용자 정보를 UserDetails 타입으로 반환
    // Spring Security는 UserDetails를 통해 사용자 인증 처리
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UserRepository에서 Soft Delete된 사용자 제외 조회
        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(
                () -> new UsernameNotFoundException("Not Found" + username)
        );

        // DB에서 조회한 User 객체를 Spring Security가 이해할 수 있는 객체(UserDetails)로 변환 및 반환
        return new UserDetailsImpl(user);
    }
}
```

---

## 역할

- Spring Security가 인증 과정에서 호출하는 서비스로, 사용자 정보 제공
- 로그인 시 또는 JWT 토큰 검증 시 호출
- Spring Security가 사용자 정보 필요 시(예: JWT 토큰 검증 후), 위 클래스가 DB에서 해당 사용자 정보를 가져온 후 Spring Security가 이해할 수 있는 형태(UserDetails)로
  변환
    - UserDetails:
        - Spring Security가 제공하는 인터페이스. 구현이 필요 즉 직접 DB 조회 불가
        - 인증 인가에 필요한 사용자 정보 표준화 객체
        - UserDetails를 통해 Spring Security가 로그인 허용 여부와 권한 체크
    - UserDetailsImpl:
        - UserDetails를 구현하여, DB에서 조회한 사용자 정보를 UserDetails 형태로 변환
        - Spring Security가 요구하는 인증/인가 정보를 제공
        - username, password, 권한(Role), 계정 상태(활성, 잠김 등) 등을 구현

## 흐름

1. 사용자가 로그인 요청 -> username/password 입력
2. Spring Security가 UserDetailsService.loadUserByUsername(username) 호출
3. 구현체(UserDetailsServiceImpl)가 DB에서 사용자 조회
4. DB에서 활성화된(user.deleted=false) 사용자 조회
5. 조회:
    - 실패 -> UsernameNotFoundException -> 로그인 실패
    - 성공 -> UserDetails 반환
6. Spring Security가 UserDetails를 이용하여,
    - 입력된 비밀번호와 DB 비밀번호 비교
    - 계정 상태 확인 (잠김/만료 여부)
    - 권한(Role) 확인 후 접근 제어