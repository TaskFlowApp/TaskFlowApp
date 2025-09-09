package com.taskflowapp.domain.team.repository;

import com.taskflowapp.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);

    /** 검색 기능 JPQL 활용 */
    /**
     * 전역 검색용 팀 상위 후보
     * name/description 부분 일치
     */
    @Query("""
  select team from Team team
  where team.deleted = false
    and ( lower(team.name)        like lower(concat('%', :q, '%'))
       or lower(team.description) like lower(concat('%', :q, '%')) )
  """)
    List<Team> searchTeamsTop(String q);

    // 팀 전체 조회 -> N+1 문제 해결 (JPQL)
    @Query("""
            select distinct t
            from Team t
            left join fetch t.members
            where t.deleted = false
    """)
    List<Team> findAllTeams();
}