package com.taskflowapp.domain.team.entity;

import com.taskflowapp.common.entity.BaseEntity;
import com.taskflowapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
@Builder
@AllArgsConstructor
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
