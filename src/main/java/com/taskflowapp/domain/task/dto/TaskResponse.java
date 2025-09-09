package com.taskflowapp.domain.task.dto;

import com.taskflowapp.domain.task.entity.Priority;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.user.dto.response.AssigneeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueDate;
    private final Priority priority;
    private final Status status;
    private final Long assigneeId;
    private final AssigneeResponse assignee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getAssignee().getId(),
                new AssigneeResponse(
                        task.getAssignee().getId(),
                        task.getAssignee().getEmail(),
                        task.getAssignee().getName(),
                        task.getAssignee().getRole()
                ),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
