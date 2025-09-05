package com.taskflowapp.domain.activity.controller;

import com.taskflowapp.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;
    // TODO : [GET /api/activites] 활동 로그 목록 조회 API 구현 예정
}
