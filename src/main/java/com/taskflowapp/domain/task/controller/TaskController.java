package com.taskflowapp.domain.task.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.dto.TaskStatusUpdateRequest;
import com.taskflowapp.domain.task.dto.TaskUpdateRequest;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    //작업 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> saveTask(
            @RequestBody TaskCreateRequest request
    ){
      TaskResponse response = taskService.createTask(request);
      ApiResponse<TaskResponse> apiResponse = ApiResponse.success("Task가 생성되었습니다.", response);
      return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    //작업 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasks(
            Pageable pageable,
            @RequestParam Status status){
        PageResponse<TaskResponse> pageResponse = taskService.getTasks(pageable, status);
        ApiResponse<PageResponse<TaskResponse>> apiResponse = ApiResponse.success("Task 목록을 조회했습니다.", pageResponse);
        return ResponseEntity.ok(apiResponse);
    }

    //작업 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(@PathVariable Long taskId){
        TaskResponse response = taskService.getTask(taskId);
        ApiResponse<TaskResponse> apiResponse = ApiResponse.success("Task를 조회했습니다.",response);
        return ResponseEntity.ok(apiResponse);
    }

    //작업 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @RequestBody TaskUpdateRequest request,
            @PathVariable Long taskId
    ){
        TaskResponse response = taskService.updateTask(request, taskId);
        ApiResponse<TaskResponse> apiResponse = ApiResponse.success("Task가 수정되었습니다.",response);
        return ResponseEntity.ok(apiResponse);
    }

    //상태 업데이트
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @RequestBody TaskStatusUpdateRequest request,
            @PathVariable Long taskId
    ){
        TaskResponse response = taskService.updateTaskStatus(request, taskId);
        ApiResponse<TaskResponse> apiResponse = ApiResponse.success("작업 상태가 업데이되었습니다.",response);
        return ResponseEntity.ok(apiResponse);
    }

    //작업 삭제
    @DeleteMapping("/{taskId}")
    public void deleteTask(
            @PathVariable Long taskId
    ){
        taskService.deleteTask(taskId);
    }
}
