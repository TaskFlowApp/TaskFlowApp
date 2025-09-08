package com.taskflowapp.domain.comment.service;

import com.taskflowapp.domain.comment.dto.request.CommentCreateRequest;
import com.taskflowapp.domain.comment.dto.response.CommentPageResponse;
import com.taskflowapp.domain.comment.dto.response.CommentResponse;
import com.taskflowapp.domain.comment.entity.Comment;
import com.taskflowapp.domain.comment.repository.CommentRepository;
import com.taskflowapp.domain.security.authuser.UserDetailsImpl;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.user.entity.User;
import com.taskflowapp.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /// Comment 생성
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

    /// Task의 Comment 목록 조회
    @Transactional(readOnly = true)
    public CommentPageResponse getAllComments(Long taskId, Pageable pageable) {
        // Task 존재 여부 확인 (실제 조회 결과는 사용하지 않음)
        taskRepository.findById(taskId);

        // 부모 댓글 페이징 조회
        Page<Comment> parentPage = commentRepository.findByTaskIdAndParentIsNull(taskId, pageable);
        List<Comment> parents = parentPage.getContent();

        // 자식 댓글(답글) 전체 조회 (부모 댓글 리스트 기준)
        List<Comment> children;
        // pageable 객체에서 createdAt 필드에 대한 정렬 방향을 가져옵니다.
        Sort.Order order = pageable.getSort().getOrderFor("createdAt");

        // 정렬 방향에 따라 자식 댓글 조회 쿼리 분기 처리
        if (order != null && order.isAscending()) {
            children = commentRepository.findChildrenByParentsOrderByCreatedAtAsc(parents);
        } else {
            children = commentRepository.findChildrenByParentsOrderByCreatedAtDesc(parents);
        }

        // 자식 댓글(답글)을 부모 ID 기준으로 그룹화
        Map<Long, List<Comment>> childrenMap = children.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParent().getId()));

        // 최종 댓글 리스트 구성 (부모 → 자식 순서로 정렬)
        List<CommentResponse> finalCommentList = new ArrayList<>();
        for (Comment parent : parents) {
            finalCommentList.add(CommentResponse.from(parent, parent.getUser(), parent.getTask().getId()));
            List<Comment> replies = childrenMap.getOrDefault(parent.getId(), List.of());
            replies.forEach(reply -> finalCommentList.add(CommentResponse.from(reply, reply.getUser(), reply.getTask().getId())));
        }

        // 페이징 정보와 함께 CommentPageResponse로 반환
        return new CommentPageResponse(
                finalCommentList,
                parentPage.getTotalElements(),
                parentPage.getTotalPages(),
                parentPage.getSize(),
                parentPage.getNumber()
        );
    }

    /// Comment 삭제
    @Transactional
    public void deleteComment(Long taskId, Long commentId, UserDetailsImpl userDetails) {

        Comment comment = findCommentById(commentId);

        if (!comment.getUser().getId().equals(userDetails.getUserId())) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        if (!comment.getTask().getId().equals(taskId)) {
            throw new RuntimeException("해당 Task의 댓글이 아닙니다.");
        }

        // 재귀적 댓글 삭제
        deleteCommentRecursively(comment);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }

    private int deleteCommentRecursively(Comment comment) {
        List<Comment> children = commentRepository.findByParentId(comment.getId());

        int count = 1; // 현재 댓글 포함
        for (Comment child : children) {
            count += deleteCommentRecursively(child); // 자식 댓글 재귀 삭제
        }
        commentRepository.delete(comment); // 현재 댓글 삭제

        return count;
    }

}
