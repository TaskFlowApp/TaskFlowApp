package com.taskflowapp.domain.comment.service;

import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentPageResponse;
import com.taskflowapp.domain.comment.dto.response.CommentResponse;
import com.taskflowapp.domain.comment.entity.Comment;
import com.taskflowapp.domain.comment.repository.CommentRepository;
import com.taskflowapp.domain.security.UserDetailsImpl;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(
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

        // 정적 팩토리 메서드 사용
        return CommentResponse.from(createdComment, user, taskId);
    }

    // Task의 Comment 목록 조회
    @Transactional(readOnly = true)
    public CommentPageResponse getAllComments(Long taskId, Pageable pageable) {

        taskRepository.findById(taskId);

        Page<Comment> parentPage = commentRepository.findByTaskIdAndParentIsNull(taskId, pageable);
        List<Comment> parents = parentPage.getContent();

        List<Comment> children = commentRepository.findByParentInOrderByCreatedAtAsc(parents);

        Map<Long, List<Comment>> childrenMap = children.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParent().getId()));

        List<CommentResponse> finalCommentList = new ArrayList<>();
        for (Comment parent : parents) {
            finalCommentList.add(CommentResponse.from(parent, parent.getUser(), parent.getTask().getId()));
            List<Comment> replies = childrenMap.getOrDefault(parent.getId(), List.of());
            replies.forEach(reply -> finalCommentList.add(CommentResponse.from(reply, reply.getUser(), reply.getTask().getId())));
        }

        return new CommentPageResponse(
                finalCommentList,
                parentPage.getTotalElements(),
                parentPage.getTotalPages(),
                parentPage.getSize(),
                parentPage.getNumber()
        );
    }
}
