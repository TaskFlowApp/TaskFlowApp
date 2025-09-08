package com.taskflowapp.common.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // JsonIncludeAnnotaionExplain.md 참고
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final Instant timestamp;        // LocalDateTimeAndInstant.md 참고

//    public ApiResponse() {}

    public ApiResponse(boolean success,
                       String message,
                       T data
    ) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    /// 튜터님 코드 정적 메서드 - 의도성이 명확
    /// ApiResponse.success("메시지", data)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    /// 각각의 메서드로 응답 메서드
//    public boolean isSuccess() {
//        return success; }
//
//    public String getMessage() {
//        return message; }
//
//    public T getData() {
//        return data; }
//
//    public Instant getTimestamp() {
//        return timestamp; }
}
