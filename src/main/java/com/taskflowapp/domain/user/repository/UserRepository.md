````
@Query("SELECT u FROM User u WHERE u.team IS NULL AND u.deleted = false") 
List<User> findAllByTeamIsNullAndDeletedFalse();

팀에 속해 있지 않은 유저만 조회   
팀에 패치조인하여 팀 정보를 가져올 필요가 없다.
WHERE u.team IS NULL : 팀이 없는 유저만 가져온다.
u.deleted = false : 삭제되지 않은 유저만 가져온다.
````     
````
@Query("SELECT u FROM User u JOIN FETCH u.team WHERE u.team.id = :teamId AND u.deleted = false")
List<User> findAllByTeamIdAndDeletedFalse(@Param("teamId") Long teamId);

팀 아이디를 기준으로 등록되어 있는 유저를 찾아서 출력
JOIN FETCH u.team : User와 Team을 가져온다.
u.team.id = :teamId : 특정 팀에 속한 유저만 조회
u.deleted = false : 삭제되지 않는 유저만 가져온다.
````
     

