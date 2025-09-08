package com.taskflowapp.domain.security;

import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final AuthUser authUser;

    public UserDetailsImpl(User user) {
        this.authUser = new AuthUser(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = authUser.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    public Long getUserId() {
        return authUser.getId();
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    /** UserDetails 인터페이스에서 아래 메서드들은 이미 구현된 디폴트 메서드
     * 구현 클래스에서 오버라이드하지 않으면, 인터페이스의 디폴트 구현이 그대로 상속됨
     * -> 필요할 때만 오버라이드해서 재정의 가능
     * => 현재 오버라이드(재정의)할 필요 X
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    */
}
