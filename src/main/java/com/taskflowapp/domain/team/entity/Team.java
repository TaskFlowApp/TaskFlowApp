package com.taskflowapp.domain.team.entity;

import com.taskflowapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String teamName;

    @Column(nullable = false, length = 255)
    private String teamDescription;

    public Team (
            String teamName,
            String teamDescription
    ){
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }


}
