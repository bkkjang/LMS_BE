# 📚 LMS_BE — 도서관리시스템 백엔드 서버

> **"걷기가 서재 - 작가의 산책"** — 누구나 작가가 되어 글을 집필·공개하는 창작 플랫폼의 백엔드입니다.
> AIVLE School AI 트랙 미니프로젝트 5차. 4차에서 만든 React 프론트엔드(LMS)의 mock 서버(json-server)를 **Spring Boot + JPA 실서버로 교체**합니다.

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-09476B?logo=h2&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)

---

## 🎯 프로젝트 개요

- 사용자가 도서를 **등록·조회·수정·삭제(CRUD)** 할 수 있는 REST API를 제공합니다.
- 도서 본문을 바탕으로 **AI 표지 이미지**를 생성해 저장합니다. (생성은 프론트 → OpenAI 직접 호출, 백엔드는 표지 URL 저장 담당)
- 4차에서 구현한 **장르 계층 · 좋아요 · 검색/필터/정렬**을 백엔드로 이관했습니다.

| 항목 | 내용 |
|---|---|
| 기본 URL | `http://localhost:8080` |
| DB | H2 (in-memory, `jdbc:h2:mem:bookdb`) |
| H2 콘솔 | `http://localhost:8080/h2-console` |
| CORS 허용 | `http://localhost:5173` (4차 React) |
| 프론트엔드 | React 19 + Vite (별도 repo, 4차 LMS) |

---

## 🛠️ 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 4.0.6, Spring MVC (REST), Spring Data JPA
- **Database**: H2 (in-memory)
- **Library**: Lombok, Spring Validation
- **Build**: Gradle

---

## 🗂️ 프로젝트 구조

```
bookapp/src/main/java/com/aivle/bookapp
├── BookappApplication.java
├── config/
│     └── WebConfig.java              ← CORS 설정 (5173 허용)
├── entity/
│     ├── Book.java                   ← @Entity, @NotBlank 검증
│     └── Genre.java                  ← @Entity, 자기참조 계층(parentCode)
├── repository/
│     ├── BookRepository.java         ← JpaRepository + 쿼리 메서드(검색·필터 조합)
│     └── GenreRepository.java
├── service/
│     ├── BookService.java            ← 비즈니스 로직, @Transactional
│     └── GenreService.java
├── controller/
│     ├── BookController.java         ← CRUD 엔드포인트, @Valid
│     └── GenreController.java
└── exception/
      ├── BookNotFoundException.java
      └── GlobalExceptionHandler.java ← @RestControllerAdvice (404/400)
```

**계층 구조**: `Controller`(요청 입구) → `Service`(로직·트랜잭션) → `Repository`(DB 접근) → `H2`

---

## 🧩 데이터 모델 (ERD)

### Book
| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| id | Long | PK, auto | 도서 번호 |
| title | String | `@NotBlank` | 제목 |
| author | String | `@NotBlank` | 저자 |
| content | String(TEXT) | `@NotBlank` | 본문(AI 프롬프트 활용) |
| genreCode | String | `@NotBlank`, FK→Genre.code | 장르 코드(예: NV-01) |
| coverImageUrl | String(TEXT) | nullable | AI 표지 Data URL |
| isLiked | boolean | 기본 false | 좋아요(내 책장) |
| createdAt | String(ISO8601) | not null | 등록 일시 |
| updatedAt | String(ISO8601) | not null | 수정 일시 |

### Genre
| 필드 | 타입 | 제약 | 설명 |
|---|---|---|---|
| code | String | PK | 장르 코드(NV, NV-01) |
| label | String | not null | 장르명(소설, 로맨스) |
| parentCode | String | nullable, FK→Genre.code | 상위 장르(null이면 대분류, 자기참조) |

**관계**: `Genre 1 : N Book` (Book.genreCode → Genre.code) · `Genre 1 : N Genre` (자기참조 계층)
> 장르는 JPA 연관관계가 아닌 **문자열 코드 컬럼**으로 보관합니다(4차 프론트가 genreCode 문자열을 직접 송수신).

---

## 🔌 API 명세

> 4차 프론트(json-server) 호출 규약과 **무수정 호환**되도록 설계했습니다.

### 도서 (Book)
| 기능 | Method | URL | Body | 상태코드 |
|---|---|---|---|---|
| 목록 조회 | GET | `/books` | - | 200 |
| 상세 조회 | GET | `/books/{id}` | - | 200 / 404 |
| 등록 | POST | `/books` | title, author, content, genreCode … | 201 / 400 |
| 수정(부분) | PATCH | `/books/{id}` | 변경 필드만 | 200 / 404 |
| 삭제 | DELETE | `/books/{id}` | - | 204 / 404 |

### 검색 / 필터 / 정렬 (`GET /books` 쿼리)
| 기능 | 예시 |
|---|---|
| 제목 검색 | `/books?title_like=별` |
| 장르 필터(세부) | `/books?genreCode=NV-01` |
| 장르 필터(대분류) | `/books?genreCode_like=NV` |
| 좋아요 필터 | `/books?isLiked=true` |
| 정렬 | `/books?_sort=createdAt&_order=desc` |

> 좋아요 토글·AI 표지 저장은 별도 엔드포인트 없이 일반 `PATCH /books/{id}`로 처리합니다.

### 장르 (Genre)
| 기능 | URL |
|---|---|
| 전체 장르 | `/genres` |
| 대분류만 | `/genres?parentCode=null` |
| 세부 장르 | `/genres?parentCode=NV` |

### 오류 응답
```json
// 400 — @Valid 검증 실패
{ "message": "제목은 필수입니다." }
// 404 — 존재하지 않는 id
{ "message": "Book not found: 99" }
```

---

## ✅ 구현 현황

### 완료
- [x] 엔티티 — `Book`, `Genre` (필드·검증·매핑)
- [x] Repository — `BookRepository`(검색·필터 쿼리 메서드 11종), `GenreRepository`
- [x] CORS 설정 — `WebConfig` (5173 허용)
- [x] 설정 — `application.yml` (H2, 포트 8080, ddl-auto: create)
- [x] 예외 클래스 — `BookNotFoundException`
- [x] 전 계층 골격 — 메서드 시그니처 통일(Controller↔Service 합의 완료)

### 진행 중 (골격 작성됨, 본문 구현 중)
- [ ] `BookController` — CRUD 5종 본문
- [ ] `BookService` — 조회/등록/수정/삭제 + 동적 검색·정렬 로직
- [ ] `GenreController` / `GenreService` — 장르 조회
- [ ] `GlobalExceptionHandler` — 404/400 응답 본문

---

## 🚧 추가로 구현되어야 할 것

1. **위 진행 중 항목의 본문 로직** (2~3일차 핵심)
   - `BookService.getBooks(...)`의 쿼리 메서드 분기 + `_sort`/`_order` 정렬 처리
   - `updateBook`의 `Map<String,Object>` 부분 수정 반영
2. **초기 데이터 시드** — 4차 `db.json`(도서 51권 + 장르 62개)을 H2에 적재
   (`data.sql` 또는 `CommandLineRunner`). 현재 시드 미포함 → 빈 DB로 기동됩니다.
3. **`@Transactional`** — Service CUD/조회에 트랜잭션 경계 적용
4. **프론트 연동** — 4차 LMS의 fetch base URL을 `8080`으로 변경(`.env`)
5. **AI 표지 E2E** — 프론트 OpenAI 호출 → 표지 Data URL을 `PATCH /books/{id}`로 저장
6. **(선택) DTO 도입** — 엔티티 직접 노출 대신 Request/Response DTO로 over-posting 차단
7. **테스트 / Postman 컬렉션** — CRUD·예외 시나리오 검증

---

## ▶️ 실행 방법

### 백엔드
```bash
cd bookapp
./gradlew bootRun
# → http://localhost:8080
# → H2 콘솔: http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:bookdb)
```

### 프론트엔드 (4차 React LMS — 별도 repo)
```bash
# fetch base URL을 http://localhost:8080 으로 변경 후
npm install
npm run dev
# → http://localhost:5173
```

---

## 👥 팀 R&R (6반 16조)

| 이름 | 주담당 | 영역 |
|---|---|---|
| 장봉경 | PM·기획 | 통합 이슈 추적 (조장) |
| 권오현 | 백엔드 | Book Entity, Repository, H2 |
| 김연주 | 백엔드 | BookService, 비즈니스 로직, @Transactional |
| 김경순 | 백엔드 | BookController, CRUD 엔드포인트, @Valid |
| 강민수 | AI·Frontend | OpenAI 표지 흐름, fetch 연동, E2E (발표) |
| 류지후 | 통합·예외 | WebConfig, GlobalExceptionHandler, QA |
| 조승대 | 설계 문서 | ERD/API 정의서, README (서기) |

---

## 🧰 트러블슈팅

| 문제 | 원인 | 해결 |
|---|---|---|
| _(작성 예정)_ | | |

> 개발 중 발생한 문제와 해결 과정을 기록해 4일차 발표·README에 활용합니다.
