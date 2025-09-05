package com.taskflowapp.domain.activity.service;

import com.taskflowapp.domain.activity.repository.ActivityRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private ActivityRepository activityRepository;
}
