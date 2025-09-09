package com.taskflowapp.domain.activity.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.common.response.PageResponse;
import com.taskflowapp.domain.activity.dto.response.ActivityResponse;
import com.taskflowapp.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ActivityResponse>>> getActivities(
            Pageable pageable,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Page<ActivityResponse> page = activityService.getActivities(pageable, type, userId, taskId, startDate, endDate);
        return ResponseEntity.ok(
                ApiResponse.success("활동 로그를 조회했습니다.", PageResponse.of(page))
        );
    }
}
