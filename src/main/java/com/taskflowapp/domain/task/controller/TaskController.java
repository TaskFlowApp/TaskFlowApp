package com.taskflowapp.domain.task.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
