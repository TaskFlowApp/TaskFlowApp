package com.taskflowapp.domain.task.service;

import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    //작업 생성
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request){
        User assignee = userRepository.findById(request.assigneeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 유저 ID입니다.")
        );
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .priority(request.getPriority())
                .assignee(assignee)
                .status(Status.TODO)
                .build();
        Task savedTask = taskRepository.save(task);
        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getDueDate(),
                savedTask.getPriority(),
                savedTask.getStatus(),
                savedTask.getAssignee().getId(),
                //savedTask.getAssignee,
                savedTask.getCreatedAt(),
                savedTask.getUpdatedAt());
    }
}
