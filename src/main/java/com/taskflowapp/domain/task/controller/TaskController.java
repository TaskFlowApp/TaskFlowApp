package com.taskflowapp.domain.task.controller;

import com.taskflowapp.domain.task.dto.TaskCreateRequest;
import com.taskflowapp.domain.task.dto.TaskCreateResponse;
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
    public ResponseEntity<TaskCreateResponse> saveTask(
            @RequestBody TaskCreateRequest request
            //@Auth 인증인가로 토큰 받아서 user..id..를... 확인해야..
    ){
      TaskCreateResponse response = taskService.createTask(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
