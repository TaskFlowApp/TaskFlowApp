package com.taskflowapp.domain.search.dto;

import com.taskflowapp.domain.task.dto.TaskResponse;
import lombok.Getter;

import java.util.List;

/**
 * 통합 검색 응답
 * - tasks: 기존 TaskResponse 재사용 (일관된 규칙 유지)
 * - users/teams: 간단한 내부 DTO로 최소 필드만 노출
 */

@Getter
public class SearchResponse {

    // DTO 네이밍/형태 레포 규칙 유지: Task는 기존 TaskResponse 재사용
    private final List<TaskResponse> tasks;

    // 사용자 users / 팀 teams 은 간단한 필드만 내리기 위해 내부 DTO 사용
    private final List<UserItem> users;
    private final List<TeamItem> teams;

    // 생성자
    public SearchResponse(List<TaskResponse> tasks,
                          List<UserItem> users,
                          List<TeamItem> teams
    ) {
        this.tasks = tasks;
        this.users = users;
        this.teams = teams;
    }

    @Getter
    public static class UserItem {
        private final Long id;
        private final String username;
        private final String name;
        private final String email;

        public UserItem(Long id,
                        String username,
                        String name,
                        String email
        ) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.email = email;
        }
    }

    @Getter
    public static class TeamItem {
        private final Long id;
        private final String name;
        private final String description;

        public TeamItem(Long id,
                        String name,
                        String description
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }
}
