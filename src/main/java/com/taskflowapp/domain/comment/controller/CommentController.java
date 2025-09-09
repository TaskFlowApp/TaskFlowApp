package com.taskflowapp.domain.comment.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentPageResponse;
import com.taskflowapp.domain.comment.dto.response.CommentResponse;
import com.taskflowapp.domain.comment.service.CommentService;
import com.taskflowapp.domain.security.authuser.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/{taskId}")
public class CommentController {

    private final CommentService commentService;

    /**
     * [ Comment 생성 ]
     * POST /api/tasks/{taskId}/comments
     */
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long taskId,
            @Validated @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails               // @AuthenticationPrincipal: Spring Security를 사용할 때, 인증된 사용자 정보(Principal)를 컨트롤러 메서드의 파라미터로 간편하게 주입해 줌
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("댓글이 생성되었습니다.", commentService.createComment(taskId, request, userDetails)));
    }

    /**
     * [ Task의 Comment 목록 조회 ]
     * GET /api/tasks/{taskId}/comments?page=0&size=10&sort=newest
     * 댓글은 계층적으로 정렬되어 반환됩니다. 부모 댓글들이 먼저 정렬되고, 각 부모 댓글 바로 다음에 해당 대댓글들이 시간순으로 배치됩니다.
     */
    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<CommentPageResponse>> getAllComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        Sort sortOrder = sort.equals("oldest")
                ? Sort.by(Sort.Order.asc("createdAt"))          //  조건 참일때, 오래된 댓글부터
                : Sort.by(Sort.Order.desc("createdAt"));        // 조건 거짓일 때, 최신 댓글부터

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        CommentPageResponse response = commentService.getAllComments(taskId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("댓글 목록을 조회했습니다.", response));
    }

    /**
     * [ Comment 삭제 ]
     * DELETE /api/tasks/{taskId}/comments/{commentId}
     * 댓글 삭제 시 해당 댓글의 모든 대댓글도 함께 삭제됩니다 (재귀적 삭제).
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String message = commentService.deleteComment(taskId, commentId, userDetails);

        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}
