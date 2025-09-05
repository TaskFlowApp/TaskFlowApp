package com.taskflowapp.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class AuthRegisterRequest {

    private String username;

    private String email;

    private String password;

    private String name;
}
