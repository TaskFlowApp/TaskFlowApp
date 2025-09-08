package com.taskflowapp.domain.user.enums;

import com.taskflowapp.domain.auth.constant.AuthConstant;

public enum UserRole {
    ADMIN,    // 관리자
    USER;      // 일반 유저

    // Spring Security 권한 체크용
    // Spring Security는 권한 문자열에 "ROLE_" 접두사가 표준. 해당 접두사가 붙어 있어야 제대로 작동
    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    /** 회원가입 시 관리자 여부
     * - 관리자
     * 유저네임: admin
     * 이메일: admin@example.com
    */
    public static UserRole fromRegistration(String username, String email) {
        if (AuthConstant.ADMIN_USERNAME.equals(username) && AuthConstant.ADMIN_EMAIL.equals(email)) {
            return ADMIN;
        }
        return USER;
    }
}
