# 📚 도서관리시스템 — Backend

KT AIVLE 5차 미니프로젝트 6반 16조  
AI를 활용한 도서 표지 이미지 생성 도서관리시스템의 Spring Boot 백엔드 서버

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA (Hibernate) |
| DB | MySQL 8 |
| 문서 | springdoc-openapi 2.8.8 (Swagger UI) |
| 빌드 | Gradle |
| 기타 | Lombok, Spring Validation |

---

## 실행 방법

### 사전 조건

- Java 17 이상
- MySQL 8 실행 중

### DB 설정

MySQL에서 별도 DB 생성은 불필요합니다.  
`createDatabaseIfNotExist=true` 옵션으로 `bookdb`가 자동 생성됩니다.

`src/main/resources/application.yml`에서 비밀번호만 수정하세요:

```yaml
spring:
  datasource:
    password: your_mysql_password
```

### 서버 실행

```bash
cd bookapp
./gradlew bootRun
```

서버가 뜨면 `data.sql`이 자동 실행되어 장르 62개 + 도서 51권 초기 데이터가 주입됩니다.

- API 서버: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## 패키지 구조

```
com.aivle.bookapp/
├── entity/
│   ├── Book.java             도서 엔티티 (@NotBlank, @PrePersist/@PreUpdate)
│   └── Genre.java            장르 엔티티 (자기참조 parentCode)
├── dto/
│   ├── BookCreateRequest.java  등록 요청 DTO
│   ├── BookUpdateRequest.java  수정 요청 DTO (PATCH용, 전 필드 nullable)
│   └── BookResponse.java       응답 DTO (Entity 직접 노출 금지)
├── repository/
│   ├── BookRepository.java   Spring Data JPA, Sort 파라미터 조합 쿼리 11종
│   └── GenreRepository.java
├── service/
│   ├── BookService.java      @Transactional, CRUD + 검색/필터/정렬/좋아요
│   └── GenreService.java
├── controller/
│   ├── BookController.java   REST 엔드포인트 6종
│   └── GenreController.java
├── exception/
│   ├── BookNotFoundException.java
│   └── GlobalExceptionHandler.java  @RestControllerAdvice, 404/400/500
└── config/
    └── WebConfig.java        CORS (허용 Origin: http://localhost:5173)
```

---

## API 엔드포인트

### 도서 (Books)

| Method | URL | 설명 | 응답 |
|---|---|---|---|
| GET | /books | 목록 조회 | 200 |
| GET | /books/{id} | 상세 조회 | 200 / 404 |
| POST | /books | 등록 | 201 / 400 |
| PATCH | /books/{id} | 부분 수정 | 200 / 404 |
| PATCH | /books/{id}/like | 좋아요 토글 | 200 / 404 |
| DELETE | /books/{id} | 삭제 | 204 / 404 |

**GET /books 쿼리 파라미터**

| 파라미터 | 설명 | 예시 |
|---|---|---|
| title_like | 제목 검색 (부분 일치) | ?title_like=해리 |
| genreCode | 세부 장르 필터 (정확 일치) | ?genreCode=NV-01 |
| genreCode_like | 대분류 필터 (접두사 일치) | ?genreCode_like=NV |
| isLiked | 좋아요 필터 | ?isLiked=true |
| _sort | 정렬 기준 (createdAt/updatedAt/title/author) | ?_sort=title |
| _order | 정렬 방향 (asc/desc, 기본 desc) | ?_order=asc |

### 장르 (Genres)

| Method | URL | 설명 |
|---|---|---|
| GET | /genres | 전체 장르 목록 |
| GET | /genres?parentCode=null | 대분류만 조회 |
| GET | /genres?parentCode=NV | 특정 대분류의 하위 장르 |

---

## 예외 처리

| 상황 | HTTP 상태 | 응답 예시 |
|---|---|---|
| 존재하지 않는 id 조회/수정/삭제 | 404 Not Found | `{"message": "Book not found: 999"}` |
| @NotBlank 등 검증 실패 | 400 Bad Request | `{"message": "title: 공백일 수 없습니다"}` |
| 서버 내부 오류 | 500 Internal Server Error | `{"message": "서버 오류가 발생했습니다."}` |

---

## 설계 결정 사항

- **DTO 분리**: Entity를 응답에 직접 노출하지 않음. `BookResponse`로 래핑하여 필드 제어
- **PATCH 단일 수정**: 표지 이미지(`coverImageUrl`), 좋아요(`isLiked`), 내용 수정을 `PATCH /books/{id}` 하나로 처리
- **타임스탬프**: `@PrePersist` / `@PreUpdate` + `Instant.now().toString()`으로 자동 관리 (`@EnableJpaAuditing` 미사용)
- **isLiked 직렬화**: `Boolean` wrapper 타입 사용 → Lombok이 `getIsLiked()` 생성 → Jackson이 `"isLiked"`로 직렬화 (primitive `boolean`은 `"liked"`로 깨짐)
- **정렬**: Spring Data `Sort` 객체를 Repository에 전달하여 DB 레벨 정렬 처리
- **CORS**: `http://localhost:5173` (React 개발 서버) 허용

---

## AI 표지 생성 흐름

Backend는 OpenAI를 직접 호출하지 않습니다.

```
Frontend → OpenAI API (gpt-image-1, 직접 호출)
        → base64 이미지 수신
        → PATCH /books/{id} { coverImageUrl: "data:image/png;base64,..." }
Backend → cover_image_url 컬럼(TEXT)에 저장
```

---

## 트러블슈팅

| 문제 | 원인 | 해결 |
|---|---|---|
| isLiked가 JSON에서 "liked"로 내려옴 | `boolean` primitive + Lombok `isLiked()` → Jackson이 `is` prefix 제거 | `Boolean` wrapper 타입으로 변경 → `getIsLiked()` 생성됨 |
| Swagger UI 500 오류 | 잘못된 springdoc 의존성 버전 | build.gradle을 springdoc-openapi-starter-webmvc-ui:2.8.8로 교체 |
