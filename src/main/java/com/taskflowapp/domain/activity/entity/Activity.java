package com.taskflowapp.domain.activity.entity;

import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "activities")
public class Activity {

    // 활동 로그 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //활동을 수행한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 활동 관련된 태스크
     * 로그인/로그아웃 등 태스크와 직접 관련 없는 경우는 null이 될 수 있음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    //활동 유형 ex)"TASK_CREATED"
    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType;

    //활동 내용 상세 설명
    @Column(nullable = false, length = 255)
    private String content;

    //로그 생성 시간
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Activity(User user, Task task, String actionType, String content) {
        this.user = user;
        this.task = task;
        this.actionType = actionType;
        this.content = content;
    }
}
