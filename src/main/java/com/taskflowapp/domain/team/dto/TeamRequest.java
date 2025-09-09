package com.taskflowapp.domain.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamRequest {

    @NotBlank(message = "팀 이름은 필수 입력 값입니다.")
    @Size(max = 50, message = "50자까지 입력 가능합니다.")
    private String name;

    @NotBlank(message = "팀 설명은 필수 입력 값입니다.")
    @Size(max = 255, message = "255자까지 입력 가능합니다.")
    private String description;

}
