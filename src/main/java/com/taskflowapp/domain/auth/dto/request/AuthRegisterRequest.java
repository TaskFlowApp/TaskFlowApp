package com.taskflowapp.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AuthRegisterRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문과 숫자만 허용됩니다.")
    private String username;

    // 기본 기능 요구사항에선 "유효한 이메일 형식"만 조건
    // -> 엄격한 검증 조건 추가
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Pattern(regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@" +
            "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?" +
            "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*" +
            "\\.[A-Za-z]{2,}$")
    private String email;

    // 기본 기능 요구사항에선 "최소 8자 이상"만 조건
    // -> 서버 자원 낭비 방지차 최대 길이 상정
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 64)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,64}$",
            message = "비밀번호는 대소문자 불문 영문, 숫자, 특수문자를 모두 포함해야 하며, 8~64자여야 합니다.")
    private String password;

    @Size(min = 2, max = 50, message = "이름은 2~50자여야 합니다.")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;
}
