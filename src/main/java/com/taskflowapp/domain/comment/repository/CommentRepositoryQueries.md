# Comment Repository 쿼리문 전체 정리

---

<h2> 자식 댓글(대댓글) 조회 (오래된 순) </h2>

```
@Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.parent IN :parents ORDER BY c.createdAt ASC")
List<Comment> findChildrenWithUserAndTaskAsc(@Param("parents") List<Comment> parents);
```

- SELECT c: 댓글(Comment) 엔티티를 조회
- JOIN FETCH c.user: 댓글 작성자(User)를 즉시 로딩 (N+1 문제 방지)
- JOIN FETCH c.task: 댓글이 속한 작업(Task)도 즉시 로딩
- WHERE c.parent IN :parents: 부모 댓글 리스트에 속한 자식 댓글만 조회
- ORDER BY c.createdAt ASC: 작성 시간 기준으로 오래된 순 정렬


----
<h2> 자식 댓글(대댓글) 조회 (최신순) </h2>

````
@Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.parent IN :parents ORDER BY c.createdAt DESC")
List<Comment> findChildrenWithUserAndTaskDesc(@Param("parents") List<Comment> parents);`
````
- SELECT c: 댓글(Comment) 엔티티를 조회
- JOIN FETCH c.user: 댓글 작성자(User)를 즉시 로딩 (N+1 문제 방지)
- JOIN FETCH c.task: 댓글이 속한 작업(Task)도 즉시 로딩
- WHERE c.parent IN :parents: 부모 댓글 리스트에 속한 자식 댓글만 조회
- ORDER BY c.createdAt DESC: 작성 시간 기준으로 최신 순 정렬


-----
<h2> 부모 댓글 페이징 조회 </h2>

````
@Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.task WHERE c.task.id = :taskId AND c.parent IS NULL")
Page<Comment> findParentCommentsWithUserAndTask(@Param("taskId") Long taskId, Pageable pageable);
````
- WHERE c.task.id = :taskId: 특정 작업(Task)에 속한 댓글만 조회
- AND c.parent IS NULL: 부모 댓글만 조회 (대댓글 제외)
- JOIN FETCH c.user, JOIN FETCH c.task: 연관 엔티티 즉시 로딩
- Pageable pageable: 페이징 및 정렬 정보 적용