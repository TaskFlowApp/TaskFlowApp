package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Priority;
import com.taskflowapp.domain.task.entity.Status;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class TaskCreateResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private final Priority priority;
    private final Status status;
    private final Long assigneeId;
    //private final UserResponse assignee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public TaskCreateResponse(Long id, String title, String description, LocalDateTime dueDate, Priority priority, Status status, Long assigneeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.assigneeId = assigneeId;
        //this.assignee = assignee
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
