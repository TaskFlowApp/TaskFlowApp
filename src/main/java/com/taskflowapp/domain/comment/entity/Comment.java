package com.taskflowapp.domain.comment.entity;

import com.taskflowapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계 설정 후 변경 예정
    @Column(nullable = false)
    private Long userId;

    // 연관관계 설정 후 변경 예정
    @Column(nullable = false)
    private Long taskId;

    // 연관관계 설정 후 변경 예정
    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false, length = 100)
    private String content;

    public Comment(Long userId, Long taskId, Long parentId, String content) {
        this.userId = userId;
        this.taskId = taskId;
        this.parentId = parentId;
        this.content = content;
    }
}
