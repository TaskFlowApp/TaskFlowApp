# AuthQueryLog
## 회원가입

```
Hibernate: 
    select
        u1_0.id 
    from
        users u1_0 
    where
        u1_0.username=? 
    limit
        ?
Hibernate: 
    select
        u1_0.id 
    from
        users u1_0 
    where
        u1_0.email=? 
    limit
        ?
Hibernate: 
    insert 
    into
        users
        (created_at, deleted, email, name, password, role, team_id, updated_at, username) 
    values
        (?, ?, ?, ?, ?, ?, ?, ?, ?)
```

## 로그인

```
// 인증 단계
// 로그인 시도 -> loadUserByUsername로 DB에서 사용자 검색
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

// 인가 단계
// 로그인 성공 -> 토큰 내 username으로 DB에서 유저 재확인
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

// 비즈니스 로직
// 컨트롤러, 서비스에서 User DB에서 조회
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

## 회원탈퇴
```
// @AuthenticationPrincipal UserDetailsImpl을 통해 현재 로그인한 사용자 정보 확인
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

// 비즈니스 로직
// 비밀번호 검증 후 회원탈퇴 처리
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
    // deleted=true하여 상태 변화
    update
        users 
    set
        deleted=?,
        email=?,
        name=?,
        password=?,
        role=?,
        team_id=?,
        updated_at=?,
        username=? 
    where
        id=?
```
