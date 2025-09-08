package com.taskflowapp.domain.task.service;

import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.dto.TaskStatusUpdateRequest;
import com.taskflowapp.domain.task.dto.TaskUpdateRequest;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.user.dto.response.AssigneeResponse;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                new AssigneeResponse(
                        savedTask.getAssignee().getId(),
                        savedTask.getAssignee().getEmail(),
                        savedTask.getAssignee().getName(),
                        savedTask.getAssignee().getRole()),
                savedTask.getCreatedAt(),
                savedTask.getUpdatedAt());
    }

    //작업 목록 조회
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getTasks(Pageable pageable, Status status){
        Page<Task> page = taskRepository.findByStatusAndDeletedFalse(status, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        Page<TaskResponse> mappedPage = page.map(task -> new TaskResponse(
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
                        task.getAssignee().getRole()),
                task.getCreatedAt(),
                task.getUpdatedAt()
        ));
        return PageResponse.of(mappedPage);
    }

    //작업 상세 조회
    @Transactional
    public TaskResponse getTask(Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getAssignee().getId(),
                new AssigneeResponse(task.getAssignee().getId(),
                        task.getAssignee().getEmail(),
                        task.getAssignee().getName(),
                        task.getAssignee().getRole()),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    //작업 수정 기능
    @Transactional
    public TaskResponse updateTask(TaskUpdateRequest request, Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 작업입니다.")
        );
        task.updateTask(request.getTitle(),request.getDescription(),request.getDueDate(),request.getPriority(),request.getStatus());
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
                        task.getAssignee().getRole()),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    //작업 상태 업데이트
    @Transactional
    public TaskResponse updateTaskStatus(TaskStatusUpdateRequest request, Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        task.updateTaskStatus(request.getStatus());
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
                        task.getAssignee().getRole()),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteTask(Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        task.softDelete();
    }
}
