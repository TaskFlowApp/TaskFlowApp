package com.taskflowapp.domain.activity.service;

import com.taskflowapp.domain.activity.dto.response.ActivityResponse;
import com.taskflowapp.domain.activity.entity.Activity;
import com.taskflowapp.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;            // final 로 변경 - @RequiredArgsConstructor 로 생성자 주입

    public Page<ActivityResponse> getActivities(
            Pageable pageable,
            String type,
            Long userId,
            Long taskId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Specification<Activity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("actionType"), type));
            }

            if (userId != null) {
                // This assumes 'user' is a relationship, so we get the ID from the related entity
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (taskId != null) {
                // This assumes 'task' is a relationship, so we get the ID from the related entity
                predicates.add(cb.equal(root.get("task").get("id"), taskId));
            }
            if (startDate != null) {
                LocalDateTime startOfDay = startDate.atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startOfDay));
            }
            if (endDate != null) {
                LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endOfDay));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return activityRepository.findAll(spec, pageable)
                .map(ActivityResponse::new);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }
}