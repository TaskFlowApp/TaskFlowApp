package com.taskflowapp.domain.task.entity;

import com.taskflowapp.common.entity.BaseEntity;
import com.taskflowapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tasks")
public class Task extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TODO;

    @Builder
    public Task ( User assignee, String title, String description, LocalDateTime dueDate, Priority priority, Status status) {
        this.assignee = assignee;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public void updateTask(String title, String description, LocalDateTime dueDate, Priority priority, Status status){
        this.title= title; this.description = description; this.dueDate=dueDate; this.priority=priority; this.status=status;}

    public void updateTaskStatus(Status status){this.status=status;}

//    @Override
//    public void softDelete() {
//        super.softDelete();
//        this.assignee = null;
//    }
}
