package com.taskflowapp.domain.comment.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentCreateResponse;
import com.taskflowapp.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @PathVariable Long taskId,
            @Validated @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails           /// @AuthenticationPrincipal: Spring Security를 사용할 때, 인증된 사용자 정보(Principal)를 컨트롤러 메서드의 파라미터로 간편하게 주입해 줌
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("댓글이 생성되었습니다.", commentService.createComment(taskId, request, userDetails.getUserId())));
    }

    /**
     * [ Task의 Comment 목록 조회 ]
     * GET /api/tasks/{taskId}/comments?page=0&size=10&sort=newest
     */

    /**
     * [ Comment 수정 (프론트 구현 X, 참고용) ]
     * PUT /api/tasks/{taskId}/comments/{commentId}
     */

    /**
     * [ Comment 삭제 ]
     * DELETE /api/tasks/{taskId}/comments/{commentId}
     */

}
