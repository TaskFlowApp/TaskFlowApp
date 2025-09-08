package com.taskflowapp.domain.dashboard.dto;

import com.taskflowapp.domain.task.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 작업 요약 응답 DTO
 * */

@Getter
public class TaskSummaryResponse {
    private final Long id;
    private final String title;
    private final Status status;
    private final LocalDateTime dueDate;

    // 생성자
    public TaskSummaryResponse(Long id,
                               String title,
                               Status status,
                               LocalDateTime dueDate
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.dueDate = dueDate;
    }
}
