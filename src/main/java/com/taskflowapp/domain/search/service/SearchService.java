package com.taskflowapp.domain.search.service;

import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.search.dto.SearchResponse;
import com.taskflowapp.domain.task.dto.TaskResponse;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.team.repository.TeamRepository;
import com.taskflowapp.domain.user.dto.response.AssigneeResponse;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 검색 서비스
 * 통합 검색(상위 N)과 작업 검색(페이지네이션)을 제공합니다.
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    /**
     * 통합(전역) 검색 (상위 5개 만 추출)
     * - Task: searchTasksTop(q, PageRequest.of(0, 5))
     * - User/Team: stream().limit(5)
     */
    public SearchResponse searchAll(String query) {
        Page<Task> taskPage = taskRepository.searchTasksTop(query, PageRequest.of(0, 5));
        List<TaskResponse> tasks = taskPage.getContent().stream()
                .map(this::toTaskResponse).toList();

        List<SearchResponse.UserItem> users = userRepository.searchUsersTop(query).stream()
                .limit(5)
                .map(user -> new SearchResponse.
                        UserItem(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail()))
                .toList();

        List<SearchResponse.TeamItem> teams = teamRepository.searchTeamsTop(query).stream()
                .limit(5)
                .map(team -> new SearchResponse.TeamItem(
                        team.getId(),
                        team.getName(),
                        team.getDescription()))
                .toList();

        return new SearchResponse(tasks, users, teams);
    }

    /**
     * 작업 검색 (페이지네이션)
     */
    public PageResponse<TaskResponse> searchTasks(String query, int page, int size) {
        Page<Task> paged = taskRepository.searchTasks(query, PageRequest.of(page, size));
        Page<TaskResponse> mapped = paged.map(
                this::toTaskResponse
        );
        return PageResponse.of(mapped);
    }

    /**
     * Entity -> DTO 매핑 : Task -> TaskResponse 매핑 (TaskService의 패턴과 동일하게 직접 구성)
     * - 엔티티를 외부로 직접 노출하지 않고, 응답 DTO(TaskResponse)로 변환합니다.
     * - assignee가 null일 수 있으므로 NPE에 주의합니다.
     */

    private TaskResponse toTaskResponse(Task task) {
        Long assigneeId = (task.getAssignee() != null) ? task.getAssignee().getId() : null;
        AssigneeResponse assignee = (task.getAssignee() == null) ? null :
                new AssigneeResponse(
                        task.getAssignee().getId(),
                        task.getAssignee().getEmail(),
                        task.getAssignee().getName(),
                        task.getAssignee().getRole()
                );
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                assigneeId,
                assignee,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
