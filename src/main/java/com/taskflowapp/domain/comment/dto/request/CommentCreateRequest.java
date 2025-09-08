package com.taskflowapp.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(max = 100, message = "댓글은 최대 100자까지 작성 가능합니다.")
    private String content;

    private Long parentId;
}
