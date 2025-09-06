package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Status;
import lombok.Getter;

@Getter
public class TaskStatusUpdateRequest {
    public Status status;
}
