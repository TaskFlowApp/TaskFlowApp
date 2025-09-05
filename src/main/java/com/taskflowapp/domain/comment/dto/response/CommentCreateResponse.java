package com.taskflowapp.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Jackson 직렬화 시 null인 필드를 JSON 결과에서 제외
// record: 컴파일러가 생성자·getter·equals/hashCode·toString을 자동으로 구현
public record CommentCreateResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        UserResponse user,
        Long parentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
