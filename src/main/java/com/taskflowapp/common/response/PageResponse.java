package com.taskflowapp.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int size;
    private final int number;

//    public PageResponse() {}

    public PageResponse(List<T> content,
                        long totalElements,
                        int totalPages, int size, int number
    ) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.number = number;
    }

    ///  튜터님 코드
    /// PageResponse.of(Page<T>) 팩토리
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),      // 원본, 메타데이터는 page에서 그대로 사용
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber()
        );
    }

    // 튜터님 코드에 추가
    /// <최근 활동 조회>
    /// 엔티티 에서 DTO로 변환된 Content 주입이 필요하면??
    public static <R> PageResponse<R> of(Page<?> page, List<R> mappedContent) {
        return new PageResponse<>(
                mappedContent,          // 가공본, dto으로 변환한 내용만 원하는 형태로 바꾸고 싶을 때 사용
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber()
        );
    }

//    public List<T> getContent() {
//        return content; }
//
//    public long getTotalElements() {
//        return totalElements; }
//
//    public int getTotalPages() {
//        return totalPages; }
//
//    public int getSize() {
//        return size; }
//
//    public int getNumber() {
//        return number; }
}