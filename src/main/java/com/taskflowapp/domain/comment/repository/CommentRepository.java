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

    // 자식 댓글 조회(대댓글) - 오래된 순 (패치 조인)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.parent IN :parents ORDER BY c.createdAt ASC")
    List<Comment> findChildrenWithUserAndTaskAsc(@Param("parents") List<Comment> parents);

    // 자식 댓글(대댓글) 조회 - 최신순 (패치 조인)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.parent IN :parents ORDER BY c.createdAt DESC")
    List<Comment> findChildrenWithUserAndTaskDesc(@Param("parents") List<Comment> parents);

    // 부모 댓글 페이징 조회 (패치 조인으로 N+1 문제 해결)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.task.id = :taskId AND c.parent IS NULL")
    Page<Comment> findParentCommentsWithUserAndTask(@Param("taskId") Long taskId, Pageable pageable);

    // 특정 댓글의 자식 댓글(대댓글) 조회 (삭제 시 사용)
    List<Comment> findByParentId(Long parentId);
}



