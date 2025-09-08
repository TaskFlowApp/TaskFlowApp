package com.taskflowapp.domain.task.repository;

import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

    public interface TaskRepository extends JpaRepository<Task, Long> {

        // 상태별 페이지 조회
        Page<Task> findByStatusAndDeletedFalse(Status status, Pageable pageable);

        Optional<Task> findByIdAndDeletedFalse(Long taskId);

        // 상태별 페이지 조회
        Page<Task> findByStatus(Status status, Pageable pageable);

        /** ===== 대시보드 ===== */
        /** --- 대시보드 통계용 카운트 메서드 --- */
        // deleted=false 전체 작업 수
        long countByDeletedFalse();
        // 특정 상태(Status)를 가지면서 삭제되지 않은 작업 수
        long countByStatusAndDeletedFalse(Status status);
        // 현재(now) 이전까지 마감되었고 아직 DONE이 아닌 작업 수
        long countByDueDateBeforeAndStatusNotAndDeletedFalse(LocalDateTime now, Status status);

        // 특정 사용자(assigneeId)의 작업 전부 (대시보드 내 작업 요약에서 날짜로 필터링)
        List<Task> findAllByAssigneeIdAndDeletedFalse(Long assigneeId);

        /** --- 팀 진행률 계산 --- */
        // 팀 진행률용 (해당 팀에 속한 멤버의 작업)
        @Query("""
      select task from Task task
      where task.deleted = false
        and task.assignee.team.id = :teamId
    """)
        List<Task> findAllByTeamId(Long teamId);

        /** ===== 검색 기능 ===== */
        /** 검색 기능 JPQL 활용 */

        /**
         * 작업 검색 (제목/설명 LIKE)
         * lower()로 소문자 변환 후 검색 → DB가 대소문자 구분할 수 있으므로 안전한 방법.
         * concat('%', :q, '%')로 부분 일치
         */

        @Query("""
    select task from Task task
    where task.deleted = false
    and ( lower(task.title) like lower(concat('%', :q, '%'))
       or lower(task.description) like lower(concat('%', :q, '%')) )
  """)
        Page<Task> searchTasks(String q, Pageable pageable);

        /**
         * 전역 검색 상단에 보여줄 상위 N개 (size=5)
         * Pageable.of(0, N)으로 개수를 제어합니다.
         */

        @Query("""
    select task from Task task
    where task.deleted = false
    and ( lower(task.title) like lower(concat('%', :q, '%'))
       or lower(task.description) like lower(concat('%', :q, '%')) )
""")
        Page<Task> searchTasksTop(String q, Pageable pageable); // 전역 검색 상위 N용 (size=5)
}