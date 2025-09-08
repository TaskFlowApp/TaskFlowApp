package com.taskflowapp.domain.task.repository;

import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatusAndDeletedFalse(Status status, Pageable pageable);
    Optional<Task> findByIdAndDeletedFalse(Long taskId);
}
