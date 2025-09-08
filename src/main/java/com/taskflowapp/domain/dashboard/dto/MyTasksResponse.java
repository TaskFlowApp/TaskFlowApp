package com.taskflowapp.domain.dashboard.dto;

import lombok.Getter;

import java.util.List;

/**
 * 내 작업 요약 응답 DTO
 * 현재 / 예정 / 연체?? 기한이 지난 작업
 * */

@Getter
public class MyTasksResponse {
    private final List<TaskSummaryResponse> todayTasks;     // 현재 작업
    private final List<TaskSummaryResponse> upcomingTasks;  // 예정 작업
    private final List<TaskSummaryResponse> overdueTasks;   // 기한이 지난 작업

    // 생성자
    public MyTasksResponse(
            List<TaskSummaryResponse> todayTasks,
            List<TaskSummaryResponse> upcomingTasks,
            List<TaskSummaryResponse> overdueTasks
    ) {
        this.todayTasks = todayTasks;
        this.upcomingTasks = upcomingTasks;
        this.overdueTasks = overdueTasks;
    }
}
