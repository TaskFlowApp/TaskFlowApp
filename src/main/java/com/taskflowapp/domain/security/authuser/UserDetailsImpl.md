# UserDetailsImpl

```java
package com.taskflowapp.domain.security;

import com.taskflowapp.domain.security.authuser.AuthUser;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
// Spring Security에서 로그인된 사용자 정보를 다루기 위해 UserDetails 인터페이스 구현
public class UserDetailsImpl implements UserDetails {

  // 사용자 정보를 담는 객체
  private final AuthUser authUser;

  // 생성자: User 객체를 받아서 AuthUser 객체에 담기 즉 변환
  // User 객체를 직접 사용하기보다, 필요한 정보(id, username, password, role)만 담은 AuthUser 객체로 변환하여 보안과 구조 분리 
  public UserDetailsImpl(User user) {
    this.authUser = new AuthUser(user);
  }

  @Override
  // 인가(권한) 검사에서 사용
  // 사용자의 권한(role)을 Spring Security가 이해할 수 있는 GrantedAuthority 객체로 변환
  // 사용자 권한 목록 반환(예: "ROLE_ADMIN", "ROLE_USER")
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // AuthUser 객체에서 UserRole(enum) 타입의 권한 정보 가져오기
    UserRole role = authUser.getRole();
    // UserRole에 정의된 getAuthority() 메서드로 "ROLE_" 접두사를 붙인 권한 문자열 생성
    // 예: UserRole.USER → "ROLE_USER"
    String authority = role.getAuthority();

    // 권한을 목록에 추가
    // 권한 문자열을 Spring Security가 이해하는 GrantedAuthority 구현체로 포장
    // SimpleGrantedAuthority는 GrantedAuthority 인터페이스의 간단한 구현체로, 내부에 String 권한을 가짐
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
    // GrantedAuthority를 담을 컬렉션을 생성한다. 여기서는 ArrayList를 사용.
    // -> List, Set 등 어떤 Collection 구현을 써도 가능
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    // 생성한 GrantedAuthority 객체를 컬렉션에 추가
    authorities.add(simpleGrantedAuthority);

    // 완성된 권한 목록을 반환 
    // Spring Security가 이 컬렉션을 사용해 권한 체크
    return authorities;
  }

  public Long getUserId() {
    return authUser.getId();
  }

  @Override
  // 인증(로그인) 과정에서 사용자의 비밀번호 반환
  // 인터페이스에 정의된 추상 메서드를 실제 로직(authUser 객체에서 값을 가져오기)으로 구현
  public String getPassword() {
    return authUser.getPassword();
  }

  @Override
  // 인증(로그인) 과정에서 사용자의 아이디(username) 반환
  public String getUsername() {
    return authUser.getUsername();
  }

  /** UserDetails 인터페이스에서 아래 메서드들은 이미 구현된 디폴트 메서드
   * 구현 클래스에서 오버라이드하지 않으면, 인터페이스의 디폴트 구현이 그대로 상속됨
   * -> 필요할 때만 오버라이드해서 재정의 가능
   * => 현재 오버라이드(재정의)할 필요 X
   @Override
   // 계정 만료 여부 반환(만료되지 않음 즉 무조건 사용 가능)
   public boolean isAccountNonExpired() {
   return true;
   }

   @Override
   // 계정 잠금 여부 반환(잠금되지 않음 즉 무조건 사용 가능)
   public boolean isAccountNonLocked() {
   return true;
   }

   @Override
   // 비밀번호 만료 여부 반환(만료되지 않음 즉 무조건 사용 가능)
   public boolean isCredentialsNonExpired() {
   return true;
   }

   @Override
   // 계정 활성화 여부 반환(활성화됨 즉 무조건 사용 가능)
   public boolean isEnabled() {
   return true;
   }
   */
}
```

---

## 역할

- Spring Security에서 로그인된 사용자 정보를 다루기 위해 UserDetails 인터페이스 구현
  - UserDetails:
    - 인증(로그인)된 사용자 정보를 담는 표준화된 구조체
    - Spring Security가 인증 인가 시 사용하는 핵심 사용자 정보 인터페이스
- DB의 User 객체를 Spring Security가 이해할 수 있도록 UserDetails 객체 형태로 변환
  - User -> UserDetails 변환 과정:
    1. DB에서 꺼낸 User 객체
    2. UserDetailsImpl로 감쌈
    3. Authentication 객체(JwtAuthFilter에서 생성)의 principal(인증된 사용자를 나타내는 객체)로 UserDetailsImpl이 들어감
    4. 해당 Authentication 객체가 SecurityContext에 저장
    5. 이후 Spring Security가 UserDetailsImpl의 메서드를 호출해 사용자 정보 및 권한 확인
- getPassword(), getUsername() 등 여러 메서드들을 강제
  - 해당 메서드들은 실제 User 객체의 정보로 채우고, 해당 정보를 바탕으로 Spring Security는 사용자의 인증 및 권한 확인
  - 인터페이스에 정의된 추상 메서드를 실제 로직으로 구현
    - 추상 메서드: 구현 없음(예: String getPassword();) -> 구현 클래스가 반드시 구현해야 함
    - 디폴트 메서드:
      - 인터페이스에서 기본 구현 제공. 필요 시 구현 클래스에서 재정의 가능
      - Java 8 이상부터 인터페이스는 디폴트 메서드를 가질 수 있음

## 추가 설명

`public Collection<? extends GrantedAuthority> getAuthorities() { ... }`
- UserDetails 인터페이스에서 메서드 시그니처가 정해져 있음 -> 구현체에서 임의 변경 시 컴파일 에러 발생
  - 메서드 시그니처:
    - 메서드를 구분 짓는 고유한 형태
    - 메서드의 이름+ 매개변수 타입 및 순서
- 추상 메서드이므로 반드시 구현해야 함
- `Collection` 사용 이유:
  - 유연성
  - `List`, `set` 등의 자료구조의 상위 인터페이스. 구체적 구현(순서 보장, 중복 허용) 등에 의존하지 않고 권한 목록 처리 가능
    - `List` 사용 시: 순서 보장
    - `Set` 사용 시: 중복 권한 제거
- `? extends GrantedAuthority`: GrantedAuthority를 구현한 모든 타입 허용