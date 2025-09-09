# 대시보드 API 컨트롤러

## 개요
 Base URL            : /api/dashboard
 인증: Authorization  : Bearer {token} 필요

 GET /stats          : 대시보드 통계 조회
 GET /my-tasks       : 내 작업 요약 조회 (오늘/예정/연체)
 GET /team-progress  : 팀별 진행률 조회
 GET /activities     : 최근 활동 내역(페이지네이션)

## [대시보드 통계 조회]
 전체 작업 수, 상태별 개수, 팀 평균 진행률, 오늘 내 작업 수, 완료율을 집계합니다.

 Endpoint
 GET /api/dashboard/stats

 Headers
 Authorization: Bearer {token}

 Response 예시
 {
   "success": true,
   "message": "대시보드 통계 조회 완료",
   "data": {
     "totalTasks": 15, "completedTasks": 8, "inProgressTasks": 4,
     "todoTasks": 3, "overdueTasks": 2, "teamProgress": 65,
     "myTasksToday": 5, "completionRate": 53
},
"timestamp": "2024-01-01T10:00:00.000Z"
}

## [내 작업 요약 조회]
사용자의 오늘 작업, 예정 작업, 기한 초과 작업을 각각 요약해서 반환합니다.

 Endpoint
 GET /api/dashboard/my-tasks

 Headers
 Authorization: Bearer {token}

 Response 필드
 - todayTasks: 오늘 처리해야 할 작업 목록
 - upcomingTasks: 향후 마감될 예정 작업 목록
 - overdueTasks: 마감 기한이 지난 작업 목록

## [팀 진행률 조회]
 팀별 완료된 작업 비율(%)을 반환합니다. 완료 상태(DONE) 기준으로 전체 대비 퍼센트를 계산합니다.

 Endpoint
 GET /api/dashboard/team-progress

 Headers
 Authorization: Bearer {token}

 응답
 {"개발팀": 75, "디자인팀": 60, "QA팀": 85}

## [최근 활동 조회 - 페이지네이션]
 사용자/작업 관련 최근 활동 로그를 최신순으로 페이지 단위로 반환합니다.

 Endpoint
 GET /api/dashboard/activities?page=0&size=10

 Headers
 Authorization: Bearer {token}

 Query Params
 - page: 페이지 번호(기본 0) 
 - size: 페이지 크기(기본 10)

 Response
 ApiResponse<PageResponse<ActivityResponse>>
