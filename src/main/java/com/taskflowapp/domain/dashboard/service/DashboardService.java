package com.taskflowapp.domain.dashboard.service;


import com.taskflowapp.domain.dashboard.dto.DashboardStatsResponse;
import com.taskflowapp.domain.dashboard.dto.MyTasksResponse;
import com.taskflowapp.domain.dashboard.dto.TaskSummaryResponse;
import com.taskflowapp.domain.task.entity.Status;
import com.taskflowapp.domain.task.entity.Task;
import com.taskflowapp.domain.task.repository.TaskRepository;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.team.repository.TeamRepository;
import com.taskflowapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 대시보드 서비스
 * 전체 통계, 내 작업 요약, 팀의 진행률을 표시
 * 읽기 전용 트랜잭션(readOnly = true)으로 조회 성능을 확보하기!
 * */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /// 1) 대시보드 전체 통계
    public DashboardStatsResponse getStats(Long userId) {
        int total = (int) taskRepository.countByDeletedFalse();
        int done = (int) taskRepository.countByStatusAndDeletedFalse(Status.DONE);
        int inProgress = (int) taskRepository.countByStatusAndDeletedFalse(Status.IN_PROGRESS);
        int todo = (int) taskRepository.countByStatusAndDeletedFalse(Status.TODO);
        int overdue = (int) taskRepository.countByDueDateBeforeAndStatusNotAndDeletedFalse(LocalDateTime.now(), Status.DONE);

        // 오늘 내 작업 수 : dueDate가 오늘인 내 작업 개수
        int myTasksToday = 0;
        if (userId != null) {
            LocalDate today = LocalDate.now();
            LocalDateTime start = today.atStartOfDay();
            LocalDateTime end = today.atTime(LocalTime.MAX);
            myTasksToday = (int) taskRepository.findAllByAssigneeIdAndDeletedFalse(userId).stream()
                    .filter(t -> t.getDueDate() != null && !t.getDueDate().isBefore(start) && !t.getDueDate().isAfter(end))
                    .count();
        }

        // 전체 완료율
        int completionRate = total == 0 ? 0 : Math.round(done * 100f / total);

        // 팀 진행률 {팀별 완료율(팀 작업 중 DONE 비율)의 단순 평균을 표시}
        List<Team> teams = teamRepository.findAll();
        List<Integer> teamRates = new ArrayList<>();
        for (Team team : teams) {
            List<Task> teamTasks = taskRepository.findAllByTeamId(team.getId());
            if (teamTasks.isEmpty()) {
                teamRates.add(0);
            } else {
                long teamDone = teamTasks.stream().filter(t -> t.getStatus() == Status.DONE).count();
                teamRates.add(Math.round(teamDone * 100f / teamTasks.size()));
            }
        }
        int teamProgress = teamRates.isEmpty() ? 0 :
                Math.round((float) teamRates.stream().mapToInt(i -> i).average().orElse(0));

        return new DashboardStatsResponse(
                total, done, inProgress, todo, overdue, teamProgress, myTasksToday, completionRate
        );
    }

    /// 2) 내 작업 요약
    public MyTasksResponse getMyTasks(Long userId) {
        if (userId == null) {
            return new MyTasksResponse(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        LocalDateTime nextWeekEnd = today.plusDays(7).atTime(LocalTime.MAX);

        List<Task> tasks = taskRepository.findAllByAssigneeIdAndDeletedFalse(userId);

        List<TaskSummaryResponse> todayTasks = tasks.stream()
                .filter(t -> t.getDueDate() != null && !t.getDueDate().isBefore(start) && !t.getDueDate().isAfter(end))
                .map(t -> new TaskSummaryResponse(t.getId(), t.getTitle(), t.getStatus(), t.getDueDate()))
                .toList();

        List<TaskSummaryResponse> upcoming = tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isAfter(end) && !t.getDueDate().isAfter(nextWeekEnd))
                .map(t -> new TaskSummaryResponse(t.getId(), t.getTitle(), t.getStatus(), t.getDueDate()))
                .toList();

        List<TaskSummaryResponse> overdue = tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(start) && t.getStatus() != Status.DONE)
                .map(t -> new TaskSummaryResponse(t.getId(), t.getTitle(), t.getStatus(), t.getDueDate()))
                .toList();

        return new MyTasksResponse(todayTasks, upcoming, overdue);
    }

    /// 3) 팀별 진행률
    public Map<String, Integer> getTeamProgress() {
        return teamRepository.findAll().stream().collect(Collectors.toMap(
                Team::getName,
                team -> {
                    List<Task> teamTasks = taskRepository.findAllByTeamId(team.getId());
                    if (teamTasks.isEmpty()) return 0;
                    long done = teamTasks.stream().filter(t -> t.getStatus() == Status.DONE).count();
                    return Math.round(done * 100f / teamTasks.size());
                }
        ));
    }
}
