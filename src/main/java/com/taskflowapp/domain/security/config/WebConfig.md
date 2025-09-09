# WebConfig

```java
package com.taskflowapp.domain.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 스프링의 설정(Configuration) 파일로 등록 -> 스프링 컨테이너에 자동 등록
@Configuration
// WebMvcConfigurer 인터페이스 구현-> 웹 설정(MVC 관련 설정) 커스터마이징
public class WebConfig implements WebMvcConfigurer {

    // CORS 설정 추가 메서드
    @Override
    public void addCorsMappings(CorsRegistry registry) { 
        // 모든 URL 경로("/**")에 대해 CORS를 적용하겠다는 의미
        registry.addMapping("/**")
                // 허용할 도메인 설정. 
                // 해당 주소("http://localhost:3000")에서 오는 요청만 허용 -> 환경변수로 관리 가능
                .allowedOrigins("http://localhost:3000")
                // 허용할 HTTP 메서드: 모두
                .allowedMethods("*")
                // 허용할 HTTP 헤더: 모두
                .allowedHeaders("*")
                // '인증 정보(쿠키, 인증 헤더 등)'를 포함한 요청 허용
                .allowCredentials(true)
                // Pre-flight 캐시 시간(단위: 초): 
                // Pre-flight 요청(실제 요청 전 OPTIONS 메서드로 보내는 사전 요청)의 결과를 3600초(1시간, 60초 * 60분) 동안 캐시
                .maxAge(3600);
    }
}
```

---

## 역할

- JWT 발급 및 검증 전 단계에서 웹 MVC 설정(WebMvcConfigurer) 중 CORS 등 웹 관련 설정 담당
- 특히 CORS 설정하는 데 사용
    - CORS(Cross-Origin Resource Sharing):
        - 웹 브라우저 보안 정책 중 하나
        - 웹 브라우저는 같은 출처(Same-Origin) 정책으로 기본적으로 다른 도메인(API 서버 등) 요청 차단
        - CORS는 위 제한을 서버가 허용해주는 메커니즘으로, 다른 도메인(예: localhost:3000)에서 보낸 요청의 허용 여부를 결정하는 규칙
        - 요청 종류:
            - Simple Request – 간단한 요청:
                - 요청 조건:
                    - GET, HEAD, POST(text/plain, application/x-www-form-urlencoded, multipart/form-data 세 가지만)
                    - 다른 출처, 같은 출처 여부 무관
                - 서버가 바로 응답 가능 즉 Pre-flight 없음
            - Pre-flight Request – 사전 요청:
                - 요청 조건:
                    - PUT, DELETE, PATCH, POST(위 세 가지 조건 외 Content-Type. 예: application/json), 커스텀 헤더
                    - 다른 출처 요청
                - 웹 브라우저가 본 요청을 보내기 전에 먼저 서버에 OPTIONS 요청을 보내서 해당 메서드 또는 헤더 허용 여부 확인
                - 서버가 허용 시, 웹 브라우저가 실제 요청 수행
                - 매번 송신 시 성능에 부담이므로, 서버가 응답 헤더에 캐시 시간을 설정하여 웹 브라우저가 같은 조건의 요청 반복 시 Pre-flight 재사용

## 필요성

- 백엔드(예: localhost:8080)와 프론트엔드(예: localhost:3000) 서버를 서로 다른 주소에서 실행 시, 웹 브라우저는 보안상의 이유로 요청 차단
- WebConfig를 통해 localhost:3000에서 오는 요청을 허용하는 것으로 설정 및 위 문제 해결

## 흐름

1. 웹 브라우저가 다른 출처(origin)에서 요청
2. Pre-flight 조건인지 판단
    - Simple Request: 서버에 바로 요청
    - Pre-flight Request: 브라우저가 먼저 OPTIONS 요청 -> 서버가 허용하면 실제 요청
3. WebConfig의 addCorsMappings가 CORS 규칙에 따라 요청 허용 또는 차단
4. 웹 브라우저가 요청 수행

## `public void addCorsMappings(CorsRegistry registry)`

- WebMvcConfigurer 인터페이스에서 제공하는 메서드 시그니처
- 오버라이드 시 Spring MVC가 해당 설정을 읽어 CorsRegistry -> CorsConfiguration으로 변환 및 저장 -> 외부에서 사용할 수 있도록 CorsConfigurationSource 인터페이스 형태로 노출
- 자동 CorsConfigurationSource 빈 생성 요건:
  - @Configuration 클래스가 존재
  - WebMvcConfigurer 구현
  - addCorsMappings 오버라이드
- 위 세 가지 만족 시, Spring Security가 http.cors(Customizer.withDefaults())를 호출 시 
  1. Spring Security는 CorsFilter 등록 
  2. CorsFilter는 요청이 들어올 때 마다 CorsConfigurationSource에서 CORS 정책을 꺼내와 확인
  - 즉 Spring MVC가 만든 CORS 정책을 Security도 꺼내 쓸 수 있게 연결 고리 제공