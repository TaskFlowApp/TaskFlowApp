package com.taskflowapp.domain.user.repository;

import com.taskflowapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /*
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username = : username AND u.deleted = false")
    Optional<User> findByUsername(@Param("username") String username);
    */
    Optional<User> findByUsernameAndDeletedFalse(String username);

    /*
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.deleted = false")
    List<User> findAll();
    */
    List<User> findAllByDeletedFalse();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // 팀에 속해 있지 않은 유저만 조회
    /*
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.team IS NULL AND u.deleted = false")
    List<User> findAllByTeamIsNull();
    */
    List<User> findAllByTeamIsNullAndDeletedFalse();

    // 팀 아이디를 기준으로 등록되어 있는 유저를 찾아서 출력
    /*
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.team.id = :teamID AND u.deleted = false")
    List<User> findAllByTeamId(@Param("teamId") Long teamId);
    */
    List<User> findAllByTeamIdAndDeletedFalse(Long teamId);
}
