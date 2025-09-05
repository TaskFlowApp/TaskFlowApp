package com.taskflowapp.domain.comment.service;

import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentCreateResponse;
import com.taskflowapp.domain.comment.entity.Comment;
import com.taskflowapp.domain.comment.repository.CommentRepository;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipal;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public CommentCreateResponse createComment(
            Long taskId,
            CommentCreateRequest request
            UserPrincipal authUser
    ) {
        Task task = taskRepository.findById(taskId);

        User user = userRepository.findById(authUser.getId());

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ChangeSetPersister.NotFoundException("Task not found (id=" + taskId + ")"));
        }

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
