package com.taskflowapp.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class AuthLoginRequest {

    private String username;

    private String password;
}
