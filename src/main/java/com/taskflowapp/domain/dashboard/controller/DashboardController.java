package com.taskflowapp.domain.dashboard.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.activity.dto.response.ActivityResponse;
import com.taskflowapp.domain.activity.service.ActivityService;
import com.taskflowapp.domain.dashboard.dto.DashboardStatsResponse;
import com.taskflowapp.domain.dashboard.dto.MyTasksResponse;
import com.taskflowapp.domain.dashboard.service.DashboardService;
import com.taskflowapp.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 대시보드 컨트롤러
 * @AuthenticationPrincipal 로 인증 헤더의 사용자 정보를 가져온다.
 * 공통 ApiResponse<T> 래퍼로 응답 형태를 통일합니다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ActivityService activityService;

    // GET /api/dashboard/stats
    // 1. 대시보드 통계
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = (userDetails == null || userDetails.getAuthUser() == null)
                ? null : userDetails.getAuthUser().getId();
        DashboardStatsResponse data = dashboardService.getStats(userId);
        return ResponseEntity.ok(ApiResponse.success("대시보드 통계 조회 완료", data));
    }

    // GET /api/dashboard/my-tasks
    // 2. 내 작업 요약
    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<MyTasksResponse>> getMyTasks(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = (userDetails == null || userDetails.getAuthUser() == null)
                ? null : userDetails.getAuthUser().getId();
        MyTasksResponse data = dashboardService.getMyTasks(userId);
        return ResponseEntity.ok(ApiResponse.success("내 작업 요약 조회 완료", data));
    }

    // GET /api/dashboard/team-progress
    // 3. 팀 진행률
    @GetMapping("/team-progress")
    public ResponseEntity<ApiResponse<Object>> getTeamProgress() {
        return ResponseEntity.ok(
                ApiResponse.success("팀 진행률 조회 완료", dashboardService.getTeamProgress())
        );
    }

    // GET /api/dashboard/activities?page=&size=
    // 4. 최근 활동 (페이지네이션)
    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<PageResponse<ActivityResponse>>> getActivities(Pageable pageable) {
        Page<ActivityResponse> page = activityService.getActivities(pageable);
        return ResponseEntity.ok(
                ApiResponse.success("활동 내역 조회 완료", PageResponse.of(page))
        );
    }
}
