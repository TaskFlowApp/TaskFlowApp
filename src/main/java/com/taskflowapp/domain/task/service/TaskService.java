package com.taskflowapp.domain.task.service;

import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.activity.entity.Activity;
import com.taskflowapp.domain.activity.service.ActivityService;
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
    private final ActivityService activityService;

    //작업 생성
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request, User user){
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

        Activity activityLog = Activity.builder()
                .user(user)
                .task(savedTask)
                .actionType("TASK_CREATED")
                .content("'" + savedTask.getTitle() + "' 업무를 생성했습니다.")
                .build();
        activityService.saveActivity(activityLog);

        return TaskResponse.from(savedTask);
    }

    //작업 목록 조회
    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getTasks(Pageable pageable, Status status, Long assigneeId){
        Page<Task> page;
        if(assigneeId != null && status != null ){
            page = taskRepository.findAllByAssigneeIdAndStatusAndDeletedFalse(assigneeId,status, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        }else if(assigneeId != null){
            page = taskRepository.findAllByAssigneeIdAndDeletedFalse(assigneeId, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        }else if(status != null) {
            page = taskRepository.findAllByStatusAndDeletedFalse(status, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        }else{
            page = taskRepository.findAllByDeletedFalse( PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
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
    public TaskResponse updateTask(TaskUpdateRequest request, Long taskId, User user){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 작업입니다.")
        );
        task.updateTask(request.getTitle(),request.getDescription(),request.getDueDate(),request.getPriority(),request.getStatus());

        Activity activityLog = Activity.builder()
                .user(user)
                .task(task)
                .actionType("TASK_UPDATED")
                .content("'" + task.getTitle() + "' 업무를 수정했습니다.")
                .build();
        activityService.saveActivity(activityLog);

        return TaskResponse.from(task);
    }

    //작업 상태 업데이트
    @Transactional
    public TaskResponse updateTaskStatus(TaskStatusUpdateRequest request, Long taskId, User user){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        String oldStatus = task.getStatus().toString();
        task.updateTaskStatus(request.getStatus());
        String newStatus = task.getStatus().toString();

        Activity activityLog = Activity.builder()
                .user(user)
                .task(task)
                .actionType("STATUS_UPDATED")
                .content("'" + task.getTitle() + "' 업무의 상태를 " + oldStatus + "에서 " + newStatus + "로 변경했습니다.")
                .build();
        activityService.saveActivity(activityLog);

        return TaskResponse.from(task);
    }

    @Transactional
    public void deleteTask(Long taskId, User user){
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 Task입니다.")
        );
        task.softDelete();

        Activity activityLog = Activity.builder()
                .user(user)
                .task(task)
                .actionType("TASK_DELETED")
                .content("'" + task.getTitle() + "' 업무를 삭제했습니다.")
                .build();
        activityService.saveActivity(activityLog);
    }
}
