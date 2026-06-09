package com.aivle.bookapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  WebConfig = 'CORS' 설정 — 다른 포트의 프론트가 이 백엔드를 호출하도록 허락하는 곳.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ CORS가 뭐고, 이게 없으면 왜 프론트가 백엔드를 못 부르나?
 *    브라우저에는 보안 규칙이 있습니다: "지금 보는 사이트와 '다른 출처(origin)'로 함부로 요청을 못 보낸다."
 *    우리: 프론트 http://localhost:5173, 백엔드 http://localhost:8080 → 포트가 달라 '다른 출처' →
 *    브라우저가 기본 차단. 그래서 백엔드가 "5173에서 오는 요청은 허락할게"라고 명시해야 하는데,
 *    그 표준 방식이 CORS(교차 출처 리소스 공유)입니다.
 *    (curl/Postman으로 부를 땐 '브라우저'가 아니라서 이 규칙이 없어 잘 됩니다. CORS는 브라우저 전용 규칙.)
 *
 *  ▶ 처음 보는 것:
 *    @Configuration : "이 클래스 안에 설정이 들어있다"는 표시.
 *    implements WebMvcConfigurer : 스프링 웹 동작을 손볼 수 있는 '약속(인터페이스)'을 구현하겠다는 선언.
 *        그중 addCorsMappings가 CORS 규칙을 등록하는 자리.
 *    @Override : 부모/인터페이스가 정한 메서드를 '내가 다시 정의(재정의)한다'는 표시.
 *
 *  ⚠️ 배포 시엔 allowedOrigins를 실제 프론트 도메인으로 바꿔야 합니다. "아무나(*) 허용"은 지양(보안).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                          // 모든 경로에 이 규칙 적용
                .allowedOrigins("http://localhost:5173")    // 허락할 프론트 출처(Vite 개발 서버)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허락할 HTTP 동작들
                .allowedHeaders("*")                        // 어떤 요청 헤더든 허용
                .allowCredentials(true)                     // 쿠키/인증정보 동반 요청 허용(로그인 대비)
                .maxAge(3600);                              // 허락 정보를 1시간(3600초) 캐시 → 매번 사전확인 요청 줄임
    }
}
