package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskCreateRequest {
    @NotBlank(message = "제목 입력은 필수입니다.")
    @Size(max = 20, message = "제목은 20자 이하로 입력해주세요.")
    public String title;

    @NotBlank(message = "작업 내용 입력은 필수입니다.")
    @Size(max = 100, message = "작업 내용은 100자 이하로 입력해주세요.")
    public String description;

    @NotBlank(message = "관리자 입력은 필수입니다.")
    public Long assigneeId;

    @NotBlank(message = "작업 기한 입력은 필수입니다.")
    public LocalDateTime dueDate;

    @NotBlank(message = "작업 중요도 입력은 필수입니다.")
    public Priority priority;
}
