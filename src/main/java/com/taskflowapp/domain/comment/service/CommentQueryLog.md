# Comment SQL Log 정리

----

[ Comment 생성 ]

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
Hibernate: 
    select
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at 
    from
        tasks t1_0 
    where
        t1_0.id=?
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
        u1_0.id=?
Hibernate: 
    insert 
    into
        comments
        (content, created_at, deleted, parent_id, task_id, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)        
```

-----
[ Comment (대댓글) 생성 ]

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
Hibernate: 
    select
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at 
    from
        tasks t1_0 
    where
        t1_0.id=?
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
        u1_0.id=?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        c1_0.updated_at,
        c1_0.user_id 
    from
        comments c1_0 
    where
        c1_0.id=?
Hibernate: 
    insert 
    into
        comments
        (content, created_at, deleted, parent_id, task_id, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
```

----
[ Task의 Comment 목록 조회 - 최신 순]

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
Hibernate: 
    select
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at 
    from
        tasks t1_0 
    where
        t1_0.id=?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at,
        c1_0.updated_at,
        c1_0.user_id,
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
        comments c1_0 
    join
        users u1_0 
            on u1_0.id=c1_0.user_id 
    join
        tasks t1_0 
            on t1_0.id=c1_0.task_id 
    where
        t1_0.id=? 
        and c1_0.parent_id is null 
    order by
        c1_0.created_at desc 
    limit
        ?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at,
        c1_0.updated_at,
        c1_0.user_id,
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
        comments c1_0 
    join
        users u1_0 
            on u1_0.id=c1_0.user_id 
    join
        tasks t1_0 
            on t1_0.id=c1_0.task_id 
    where
        c1_0.parent_id in (?) 
    order by
        c1_0.created_at desc
```

---
[ Task의 Comment 목록 조회 - 오래된 순]

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
Hibernate: 
    select
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at 
    from
        tasks t1_0 
    where
        t1_0.id=?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at,
        c1_0.updated_at,
        c1_0.user_id,
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
        comments c1_0 
    join
        users u1_0 
            on u1_0.id=c1_0.user_id 
    join
        tasks t1_0 
            on t1_0.id=c1_0.task_id 
    where
        t1_0.id=? 
        and c1_0.parent_id is null 
    order by
        c1_0.created_at 
    limit
        ?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        t1_0.id,
        t1_0.assignee_id,
        t1_0.created_at,
        t1_0.deleted,
        t1_0.description,
        t1_0.due_date,
        t1_0.priority,
        t1_0.status,
        t1_0.title,
        t1_0.updated_at,
        c1_0.updated_at,
        c1_0.user_id,
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
        comments c1_0 
    join
        users u1_0 
            on u1_0.id=c1_0.user_id 
    join
        tasks t1_0 
            on t1_0.id=c1_0.task_id 
    where
        c1_0.parent_id in (?) 
    order by
        c1_0.created_at        
```

---

[ Comment 삭제 ]
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
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        c1_0.updated_at,
        c1_0.user_id 
    from
        comments c1_0 
    where
        c1_0.id=?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        c1_0.updated_at,
        c1_0.user_id 
    from
        comments c1_0 
    left join
        comments p1_0 
            on p1_0.id=c1_0.parent_id 
    where
        p1_0.id=?
Hibernate: 
    select
        c1_0.id,
        c1_0.content,
        c1_0.created_at,
        c1_0.deleted,
        c1_0.parent_id,
        c1_0.task_id,
        c1_0.updated_at,
        c1_0.user_id 
    from
        comments c1_0 
    left join
        comments p1_0 
            on p1_0.id=c1_0.parent_id 
    where
        p1_0.id=?
Hibernate: 
    delete 
    from
        comments 
    where
        id=?
Hibernate: 
    delete 
    from
        comments 
    where
        id=?      
```