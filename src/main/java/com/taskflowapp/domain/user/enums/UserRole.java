package com.taskflowapp.domain.user.enums;

public enum UserRole {
    ADMIN,    // 관리자
    USER;      // 일반 유저

    // Spring Security 권한 체크용
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
