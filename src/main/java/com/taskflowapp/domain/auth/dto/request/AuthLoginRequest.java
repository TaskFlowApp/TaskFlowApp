package com.taskflowapp.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthLoginRequest {

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
