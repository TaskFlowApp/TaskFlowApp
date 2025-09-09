package com.taskflowapp.domain.task.service;

import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.dto.TaskStatusUpdateRequest;
import com.taskflowapp.domain.task.dto.TaskUpdateRequest;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;

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
        User assignee = userRepository.findByIdAndDeletedFalse(request.assigneeId).orElseThrow(
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
        return TaskResponse.from(savedTask);
    }

    //작업 목록 조회
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getTasks(Pageable pageable, Status status, Long assigneeId){
        Page<Task> page;
        if(assigneeId != null && status != null ){
            page = taskRepository.findAllByAssigneeIdAndStatusAndDeletedFalse(assigneeId,status, pageable);
        }else if(assigneeId != null){
            page = taskRepository.findAllByAssigneeIdAndDeletedFalse(assigneeId, pageable);
        }else if(status != null) {
            page = taskRepository.findAllByStatusAndDeletedFalse(status, pageable);
        }else{
            page = taskRepository.findAllByDeletedFalse( pageable);
        }
        Page<TaskResponse> mappedPage = page.map(TaskResponse::from);
        return PageResponse.of(mappedPage);
    }

    //작업 상세 조회
    @Transactional
    public TaskResponse getTask(Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        return TaskResponse.from(task);
    }

    //작업 수정 기능
    @Transactional
    public TaskResponse updateTask(TaskUpdateRequest request, Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 작업입니다.")
        );
        task.updateTask(request.getTitle(),request.getDescription(),request.getDueDate(),request.getPriority());
        return TaskResponse.from(task);
    }

    //작업 상태 업데이트
    @Transactional
    public TaskResponse updateTaskStatus(TaskStatusUpdateRequest request, Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        task.updateTaskStatus(request.getStatus());
        return TaskResponse.from(task);
    }

    @Transactional
    public void deleteTask(Long taskId){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        task.softDelete();
    }
}
