package com.taskflowapp.domain.task.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tasks")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "assignee_id", nullable = false)
//    private User assignee;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Task (
            //User assignee,
            String title, String description, LocalDate dueDate, Priority priority, Status status) {
        //this.assignee = assignee;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public static Task of(
                        //User assignee
                          String title, String description, LocalDate dueDate, Priority priority, Status status
    ){return new Task(
                //assignee,
                title, description,dueDate,priority,status);
    }

    public void updateTask(String title, String description, LocalDate dueDate, Priority priority, Status status){
        this.title= title; this.description = description; this.dueDate=dueDate; this.priority=priority; this.status=status;}

    public void updateTaskStatus(Status status){this.status=status;}
}
