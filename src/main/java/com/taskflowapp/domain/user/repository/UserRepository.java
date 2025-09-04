package com.taskflowapp.domain.user.repository;

import com.taskflowapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
