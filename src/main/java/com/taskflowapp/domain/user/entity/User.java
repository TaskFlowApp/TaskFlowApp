package com.taskflowapp.domain.user.entity;

import com.taskflowapp.common.entity.BaseEntity;
import com.taskflowapp.domain.team.entity.Team;
import com.taskflowapp.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 각자 작업 진행 중이라 우선 주석 처리 -> 향후 주석풀기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public User(
            String email,
            String username,
            String name,
            String password,
            UserRole role
    ) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void changeTeam(Team team) {
        this.team = team;
    }

    @Override
    public void softDelete() {
        super.softDelete();    // 부모 Soft Delete 메서드의 deleted = true 처리 실행
        this.team = null;      // 연관관계 끊기
    }
}
