package com.taskflowapp.domain.search.controller;

import com.taskflowapp.common.response.ApiResponse;
import com.taskflowapp.domain.search.dto.SearchResponse;
import com.taskflowapp.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 검색 컨트롤러
 * - /api/search  : 통합 검색 (상위 N)
 * - /api/tasks/search : 작업 검색(페이지네이션) - task 컨트롤러에서!
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    /**
     * 통합 검색 (tasks/users/teams)
     * GET /api/search?q={query}
     *
     */
    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> searchAll(
            @RequestParam("q") String q                                 // q : 검색어(필수)
    ) {
        SearchResponse data = searchService.searchAll(q);
        return ResponseEntity.ok(ApiResponse.success("검색 완료", data));
    }
}
