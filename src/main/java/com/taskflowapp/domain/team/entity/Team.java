package com.taskflowapp.domain.team.entity;

import com.taskflowapp.common.entity.BaseEntity;
import com.taskflowapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
@Builder
@AllArgsConstructor
// 소프트딜리트 적용 위해 추가
// DB에 남아있는 삭제된 팀을 검색에서 제외하기 위함
// Hibernate가 자동으로 조건 붙여줌
@Where(clause = "deleted = false")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @OneToMany(mappedBy = "team")
    private List<User> members; // 멤버의 리스트를 참조하기 위해

    // 양방향 연관관계 동기화 멤버 추가
    public void addMember(User user) {
        members.add(user);
        user.changeTeam(this);
    }

    // 양방향 연관관계 동기화 멤버 제거
    public void removeMember(User user) {
        members.remove(user);
        user.changeTeam(null);
    }

    public Team (
            String name,
            String description
    ){
        this.name = name;
        this.description = description;
    }

    // 팀 수정 메서드 //
    public void changeTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
