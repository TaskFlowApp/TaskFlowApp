# AuthUser

```java
package com.taskflowapp.domain.security;

import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthUser {

    // DB의 User 엔티티에서 가져온 정보 담음
    private Long id;
    private String email;
    private String username;
    private String name;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User 엔티티 객체를 받아서 AuthUser 객체의 필드에 정보를 옮겨 담음
    public AuthUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.name = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
```

---

## 역할

- User 엔티티를 UserDetails에 넣기 적합한 형태로 변환한 DTO
- UserDetailsImpl이 내부에서 AuthUser를 참조하여 getUsername(), getPassword(), getRole() 등을 제공
