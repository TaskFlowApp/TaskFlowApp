package com.taskflowapp.domain.task.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.search.service.SearchService;
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
    private final SearchService searchService;      // 작업 검색 페이징을 위해서 넣음

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
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long taskId
    ){
        taskService.deleteTask(taskId);
        ApiResponse<Void> response = ApiResponse.success("Task가 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    /**
     * 작업 검색 (페이징)
     * 엔드포인트 : GET /api/tasks/search?q={query}&page=0&size=10
     */
    // todo `/api/tasks/search` 경로를 유지하기 위해 TaskController 에 둘지 확인
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> searchTasks(
            @RequestParam("q") String q,                                // q : 검색어(필수)
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageResponse<TaskResponse> data = searchService.searchTasks(q, page, size);
        return ResponseEntity.ok(ApiResponse.success("작업 검색 완료", data));
    }
}
