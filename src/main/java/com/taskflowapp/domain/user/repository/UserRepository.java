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

    Optional<User> findByIdAndDeletedFalse(Long userId);

    List<User> findByDeletedFalse();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // 팀에 속해 있지 않은 유저만 조회
    @Query("SELECT u FROM User u WHERE u.team IS NULL AND u.deleted = false")
    List<User> findAvailableUsers();

    // 팀 아이디를 기준으로 등록되어 있는 유저를 찾아서 출력
    @Query("SELECT u FROM User u JOIN FETCH u.team WHERE u.team.id = :teamId AND u.deleted = false")
    List<User> findTeamMembers(@Param("teamId") Long teamId);

    // 검색 기능 JPQL 활용
    /**
     * 전역 검색용 사용자 상위 후보
     * username / name / email 부분 일치
     * 상위 N 제한은 검색 서비스(SearchService)에서 stream().limit(N)로 처리
     */
    @Query("""
    select user from User user
    where user.deleted = false
    and ( lower(user.username) like lower(concat('%', :q, '%'))
       or lower(user.name)     like lower(concat('%', :q, '%'))
       or lower(user.email)    like lower(concat('%', :q, '%')) )
    """)
    List<User> searchUsersTop(String q);
    // 상위 5개는 서비스에서 stream().limit(5)
}