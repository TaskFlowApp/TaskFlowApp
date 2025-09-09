# UserQueryLog
## 현재 사용자 정보 조회
```
Hibernate: 
    select
        u1_0.id,
        u1_0.created_at,
        u1_0.deleted,
        u1_0.email,
        u1_0.name,
        u1_0.password,
        u1_0.role,
        u1_0.team_id,
        u1_0.updated_at,
        u1_0.username 
    from
        users u1_0 
    where
        u1_0.username=? 
        and not(u1_0.deleted)
```