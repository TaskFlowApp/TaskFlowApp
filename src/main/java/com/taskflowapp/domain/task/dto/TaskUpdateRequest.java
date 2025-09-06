package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Priority;
import com.taskflowapp.domain.task.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskUpdateRequest {
    public String title;
    public String description;
    public LocalDateTime dueDate;
    public Priority priority;
    public Status status;
    public Long assigneeId;
}
