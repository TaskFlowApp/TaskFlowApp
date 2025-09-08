package com.taskflowapp.domain.comment.entity;

import com.taskflowapp.common.entity.BaseEntity;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true) // null 허용
    private Comment parent;

    @Column(nullable = false, length = 100)
    private String content;

    @Builder
    public Comment(User user, Task task, Comment parent, String content) {
        this.user = user;
        this.task = task;
        this.parent = parent;
        this.content = content;
    }
}
