package com.taskflowapp.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taskflowapp.domain.comment.entity.Comment;
import com.taskflowapp.domain.user.dto.response.MemberResponseDto;
import com.taskflowapp.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Jackson 직렬화 시 null인 필드를 JSON 결과에서 제외
// record: 컴파일러가 생성자·getter·equals/hashCode·toString을 자동으로 구현
public record CommentResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        MemberResponseDto user,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // 정적 팩토리 메서드
    public static CommentResponse from(Comment comment, User user, Long taskId) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                taskId,
                user.getId(),
                MemberResponseDto.fromWithoutCreatedAt(user),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
