package com.taskflowapp.domain.activity.service;

import com.taskflowapp.domain.activity.dto.response.ActivityResponse;
import com.taskflowapp.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private ActivityRepository activityRepository;

    public Page<ActivityResponse> getActivities(Pageable pageable) {
        // TODO : 추후 명세서에 따른 필터링 조건 추가 구현
        return activityRepository.findAll(pageable)
                .map(ActivityResponse::new);
    }
}
