
# LocalDateTime 과 Instant 확인 해보기
## LocalDateTime:
‘연-월-일 시:분:초’만 있고 시간대/오프셋이 없음. 예: 2025-09-05T10:30. 사람 기준의 “그곳의 시각” 표현용. 같은 값이라도 어느 지역인지 모르면 언제인지 확정 못 함(DST 애매함 포함).

### LocalDateTime 사용하기 적합한 곳
글로벌 서비스가 아닌 단일 리전 서비스 일 때(타임존 X)
즉, 특정 날짜와 시간을 여러 리전에서 적용하려는 경우
FrontEnd Service, 즉 Display(View) 에 좋다고 한다.

## Instant:
 UTC 기준의 절대 시점(에포크부터의 초/나노). 전 세계 어디서든 유일한 “그때”. 기계가 기록·정렬·비교하기 좋음.

### Instant 사용하기 적합한 곳
 Timestamp를 UTC 형식으로 저장하는 곳 즉 DB, 벡엔드 비즈니스 로직, 데이터 교환, 직렬화 시나리오에 적합하다. (연산하기 쉽기 때문)
 글로벌 런칭한 서비스 비즈니스 앱 개발 시 Instant 나 ZonedDateTime 클래스 (ZoneId+ Instant )를 많이 사용한다.

## 변환:
 LocalDateTime -> Instant: 시간대가 필요함 → ldt.atZone(ZoneId.of("Asia/Seoul")).toInstant()
 Instant -> LocalDateTime: 보기용 지역 시각으로 → instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()

 ## 요약:
 표현(사람용) = LocalDateTime
 기록/비교(절대시점) = Instant