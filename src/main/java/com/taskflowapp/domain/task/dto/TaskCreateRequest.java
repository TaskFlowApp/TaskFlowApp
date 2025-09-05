package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Priority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskCreateRequest {
    public String title;
    public String description;
    public Long assigneeId;
    public LocalDateTime dueDate;
    public Priority priority;
}
