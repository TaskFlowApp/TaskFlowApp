# 검색 API 컨트롤러
 
## 개요
 Base URL               : /api/search
 인증: Authorization     : Bearer {token} 필요

 GET /api/search        : 통합 검색 (작업/사용자/팀 상위 N)
 GET /api/tasks/search  : 작업 검색 (페이지네이션) — TaskController

## [통합 검색]
 작업/사용자/팀에서 검색어에 매칭되는 상위 결과를 모아 반환합니다.

 Endpoint
 GET /api/search?q={query}

 Query Params
 - <code>q</code>: 검색어(필수)

 Response
 ApiResponse<SearchResponse>

## [작업 검색 - 페이징네이션]      - TaskController
제목/설명 기반 키워드 검색을 수행하고 결과를 페이지 단위로 반환합니다.

 Endpoint
 GET /api/tasks/search?q={query}&page=0&size=10

 Query Params
 - q: 검색어(필수)
 - page: 페이지 번호(기본 0)
 - size: 페이지 크기(기본 10)

 Response
 ApiResponse<PageResponse<TaskResponse>>
