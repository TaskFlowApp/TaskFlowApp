package com.taskflowapp.domain.comment.service;

import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentCreateResponse;
import com.taskflowapp.domain.comment.entity.Comment;
import com.taskflowapp.domain.comment.repository.CommentRepository;
import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.user.dto.response.UserResponse;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentCreateResponse createComment(
            Long taskId,
            CommentCreateRequest request,
            UserDetailsImpl userDetails
    ) {
        // Task 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));

        // User 조회
        Long userId = userDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // Parent Comment 조회 (답글 체크)
        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        }

        // 댓글 생성
        Comment comment = Comment.builder()
                .user(user)
                .task(task)
                .parent(parent)
                .content(request.getContent())
                .build();
        Comment createdComment = commentRepository.save(comment);

        return CommentCreateResponse.builder()
                .id(createdComment.getId())
                .content(createdComment.getContent())
                .taskId(task.getId())
                .userId(user.getId())
                .user(
                        UserResponse.builder()
                                .id
                                .username
                                .name
                                .email
                                .role
                                .build()
                )
                .parentId(parent != null ? parent.getId() : null)       /// parent가 존재하면 parentId를 설정, parent가 없으면 null 처리
                .createdAt(createdComment.getCreatedAt())
                .updatedAt(createdComment.getUpdatedAt())
                .build();
    }
}
