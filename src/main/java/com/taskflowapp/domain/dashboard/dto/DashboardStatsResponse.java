package com.taskflowapp.domain.dashboard.dto;

import lombok.Getter;

/**
 * 대시보드 통계 표시용 응답 DTO
 * 전체 작업, 완료율, 팀 평균 진행률 등을 표시
 * */

@Getter
public class DashboardStatsResponse {
    private final int totalTasks;
    private final int completedTasks;
    private final int inProgressTasks;
    private final int todoTasks;
    private final int overdueTasks;
    private final int teamProgress;     // 전체 팀 평균 완료율(%) (단순 평균)
    private final int myTasksToday;     // 오늘 내 작업 수
    private final int completionRate;   // 전체 완료율(%)

    // 생성자
    public DashboardStatsResponse(
            int totalTasks,
            int completedTasks,
            int inProgressTasks,
            int todoTasks,
            int overdueTasks,
            int teamProgress,
            int myTasksToday,
            int completionRate
    ) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.inProgressTasks = inProgressTasks;
        this.todoTasks = todoTasks;
        this.overdueTasks = overdueTasks;
        this.teamProgress = teamProgress;
        this.myTasksToday = myTasksToday;
        this.completionRate = completionRate;
    }
}
