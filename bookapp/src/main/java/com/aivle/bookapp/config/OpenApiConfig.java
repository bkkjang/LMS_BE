package com.aivle.bookapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  OpenApiConfig = Swagger(API 문서·테스트 화면)의 '제목/설명'을 지정하는 설정.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ Swagger / OpenAPI 가 뭔가요? (백엔드 처음이면)
 *    - 우리가 만든 API 목록(GET /api/books 등)을 '자동으로' 보기 좋은 웹페이지로 만들어 주는 도구입니다.
 *    - build.gradle 에 springdoc 라이브러리만 넣으면, @RestController·@GetMapping·DTO 들을 스캔해
 *      "이 서버엔 이런 API가 있고, 어떤 값을 받고 돌려준다"를 문서로 그려 줍니다. (우리가 글로 안 써도 됨)
 *    - 게다가 그 화면에서 'Try it out' 버튼으로 실제 요청을 보내 볼 수 있어요 → curl 없이 브라우저로 테스트.
 *
 *  ▶ 접속 주소 (서버 띄운 뒤):
 *      http://localhost:8080/swagger-ui.html   ← 사람이 보는 테스트 화면
 *      http://localhost:8080/v3/api-docs        ← 기계가 읽는 원본(JSON). 보통 직접 볼 일은 없음.
 *
 *  ▶ 이 클래스는 '필수'가 아닙니다.
 *    라이브러리만 넣어도 Swagger는 동작해요. 다만 제목이 밋밋하게 나와서, 아래처럼 '문서 표지'(제목·설명·
 *    버전)를 직접 지정해 주는 것뿐입니다. (@Bean = 스프링이 만들어 관리할 객체를 직접 등록하는 방법)
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookappOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookapp API")
                        .description("도서·장르 관리 백엔드 API 문서 (책 등록/검색/수정/삭제, 장르 조회)")
                        .version("v0.0.1"));
    }
}
