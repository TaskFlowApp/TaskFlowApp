package com.taskflowapp.domain.team.repository;

import com.taskflowapp.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
