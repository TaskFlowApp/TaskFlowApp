package com.taskflowapp.domain.activity.repository;

import com.taskflowapp.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
