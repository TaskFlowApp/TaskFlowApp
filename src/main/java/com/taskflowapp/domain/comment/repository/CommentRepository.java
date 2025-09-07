package com.taskflowapp.domain.comment.repository;

import com.taskflowapp.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskIdAndParentIsNull(Long taskId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.parent IN :parents ORDER BY c.createdAt ASC")
    List<Comment> findChildrenByParentsOrderByCreatedAtAsc(@Param("parents") List<Comment> parents);

    @Query("SELECT c FROM Comment c WHERE c.parent IN :parents ORDER BY c.createdAt DESC")
    List<Comment> findChildrenByParentsOrderByCreatedAtDesc(@Param("parents") List<Comment> parents);

    List<Comment> findByParentId(Long parentId);
}



