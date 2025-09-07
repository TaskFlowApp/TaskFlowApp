package com.taskflowapp.domain.user.repository;

import com.taskflowapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // 팀에 속해 있지 않은 유저만 조회
    List<User> findAllByTeamIsNull();

    // 팀 아이디를 기준으로 등록되어 있는 유저를 찾아서 출력
    List<User> findAllByTeamId(Long teamId);
}
