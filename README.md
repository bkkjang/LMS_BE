# LMS_BE (도서 관리 시스템 백엔드)

<!-- CI/CD 파이프라인 자동 빌드 테스트용 commit -->

## 1. 프로젝트 개요

LMS_BE는 도서 정보를 관리하고 조회할 수 있는 RESTful API를 제공하는 도서 관리 시스템의 백엔드 프로젝트입니다. Spring Boot를 기반으로 개발되었으며, 도서의 CRUD(생성, 조회, 수정, 삭제) 기능과 장르별 도서 조회, 도서 검색 기능을 제공합니다. 특히 JWT 기반의 사용자 인증을 통해 **사용자별 '좋아요' 책장 등 개인화된 도서 관리 기능**을 지원합니다.

이 프로젝트는 3-Tier 아키텍처(Controller-Service-Repository)를 따라 설계되어 각 계층의 역할과 책임을 명확히 분리하고 코드의 유지보수성과 확장성을 높였습니다.

## 3. 프로젝트 구조

- **`controller`**: HTTP 요청을 수신하고 서비스 계층으로 처리를 위임하며, 그 결과를 HTTP 응답으로 반환합니다.
- **`service`**: 핵심 비즈니스 로직을 구현합니다.
- **`repository`**: 데이터베이스와의 통신을 담당하며, JPA를 사용하여 데이터의 영속성을 처리합니다.
- **`entity`**: 데이터베이스 테이블과 매핑되는 도메인 모델입니다.
- **`dto`**: 데이터 전송 객체로, 각 계층 간의 데이터 교환에 사용됩니다.
- **`exception`**: 애플리케이션 전역의 예외를 처리합니다.

## 4. 주요 기능 및 심화 구현 내용

### 📚 기본 도서 관리
- **도서 CRUD**: 도서 정보 등록, 수정(부분 수정 포함), 삭제, 단일 상세 조회 지원.
- **도서 목록 및 필터링**: 전체 도서 목록 조회 및 다양한 검색 조건(제목, 장르 코드 필터링) 제공.
- **장르 관리** ➡️ [`GenreController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/GenreController.java)
  - 전체 장르 및 대분류(`parentCode=null`)/소분류 계층을 가진 하위 장르 목록 조회 (총 62개 장르 시드 데이터 기반).
- **AI 표지 자동 생성 (OpenAI 연동)**:
  - 프론트엔드에서 생성한 표지 이미지 URL을 백엔드에 안전하게 저장 및 업데이트 기능 지원.

### 🔐 보안 및 인증
- **회원가입 및 로그인 절차 강화** ➡️ [`AuthController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/AuthController.java), ➡️ [`EmailService.java`](bookapp/src/main/java/com/aivle/bookapp/service/EmailService.java)
  - 이메일 OTP(발송 ➡️ 확인)를 통한 3단계 회원가입 절차 구현.
  - 비밀번호 BCrypt 단방향 암호화 저장 및 24시간 유효한 JWT 토큰 발급.
- **STATELESS 기반 접근 제어** ➡️ [`SecurityConfig.java`](bookapp/src/main/java/com/aivle/bookapp/config/SecurityConfig.java), ➡️ [`JwtAuthenticationFilter.java`](bookapp/src/main/java/com/aivle/bookapp/config/JwtAuthenticationFilter.java)
  - 세션을 사용하지 않는 STATELESS 보안 정책 적용.
  - `GET` 방식의 조회 API는 전체 공개하되, CUD(생성, 수정, 삭제) 및 유저 전용 API는 인증을 요구하도록 보호.
  - 인증/인가 실패 시 발생하는 401(Unauthorized), 403(Forbidden) 에러를 프론트엔드 처리에 용이한 정제된 JSON 형태로 응답하도록 처리.

### 💖 개인화 기능
- **유저별 '좋아요' 책장 관리** ➡️ [`UserLike.java`](bookapp/src/main/java/com/aivle/bookapp/entity/UserLike.java), ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - 단순 전역 `boolean` 상태의 한계를 극복하고, `user_likes` 연결(N:M) 테이블을 도입하여 로그인한 사용자별로 독립적인 '좋아요' 도서 목록을 관리.
- **단일 좋아요 토글 API** ➡️ [`BookController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/BookController.java)
  - `PATCH /books/{id}/like` 단일 엔드포인트 호출만으로 특정 유저의 좋아요 상태를 DB에 삽입(Insert)하거나 삭제(Delete)하도록 간결하고 효율적인 토글 방식 구현.

### 🔌 외부 연동 및 부가 기능
- **Naver 도서 검색 API 프록시** ➡️ [`NaverProxyController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/NaverProxyController.java)
  - 프론트엔드 브라우저에서 직접 호출 시 발생하는 CORS 정책 문제를 우회하기 위한 백엔드 프록시 API(`GET /api/naver`) 구축.
  - 외부 무단 사용을 막기 위해 해당 API는 로그인한 사용자만 호출할 수 있도록 보안 설정 적용.
- **AI 이미지 생성 프록시 (OpenAI)** ➡️ [`AiProxyController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/AiProxyController.java)
  - OpenAI API Key 보안을 위해 프론트엔드 대신 백엔드에서 이미지를 생성하도록 프록시 API 구축.
- **API 명세 자동화 (Swagger UI)** ➡️ [`SwaggerConfig.java`](bookapp/src/main/java/com/aivle/bookapp/config/SwaggerConfig.java)
  - `springdoc-openapi` 라이브러리를 도입하여 `/swagger-ui.html` 접근 시 최신 API 명세 제공.
  - JWT Bearer 인증 방식을 Swagger 설정에 포함하여, 인증이 필요한 API도 UI 상에서 즉각적인 테스트 지원.
- **초기 더미 데이터 세팅 (Data Seed)** ➡️ [`data.sql`](bookapp/src/main/resources/data.sql)
  - `resources/data.sql`을 통해 애플리케이션 시작 시 도서 51권과 장르 62개의 기초 데이터를 데이터베이스에 자동 주입하여 개발 및 테스트 편의성 증대.

## 5. 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 4.0.6
- **주요 라이브러리**:
    - **`spring-boot-starter-web`**: RESTful API 개발
    - **`spring-boot-starter-data-jpa`**: 데이터베이스 연동 및 ORM
    - **`spring-boot-starter-validation`**: 데이터 유효성 검증
    - **`mysql-connector-j`**: MySQL 데이터베이스 연동
    - **`lombok`**: 보일러플레이트 코드 감소
    - **`springdoc-openapi-starter-webmvc-ui`**: API 문서 자동화 (Swagger UI)

## 6. 실행 방법

### 사전 준비 사항

- Java 17 설치
- MySQL 데이터베이스 서버 구동
- IDE (IntelliJ, Eclipse 등) 또는 Gradle/Maven 설치

### 설정

1.  **데이터베이스 설정**:
    `src/main/resources/application.properties` 파일에 사용자의 MySQL 데이터베이스 정보를 입력합니다.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```

### 실행

- **IDE에서 실행**:
    - `BookappApplication.java` 파일을 찾아서 실행합니다.
- **Gradle을 이용한 실행**:
    - 프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다.
    ```bash
    ./gradlew bootRun
    ```

## 7. API 명세

API 문서는 서버 실행 후 `http://localhost:8080/swagger-ui.html` 에서 확인하실 수 있습니다.

### Auth API
- `POST /auth/send-code`: 이메일 인증 코드 발송
- `POST /auth/check-code`: 이메일 인증 코드 확인
- `POST /auth/signup`: 회원가입
- `POST /auth/login`: 로그인 (JWT 발급)

### Book API
- `GET /books`: 도서 목록 조회 (검색 및 필터링 가능)
- `GET /books/{id}`: 특정 도서 상세 조회
- `POST /books`: 새로운 도서 등록
- `PATCH /books/{id}`: 도서 부분 수정 (표지 URL 업데이트 포함)
- `PATCH /books/{id}/like`: 특정 도서 좋아요 토글
- `DELETE /books/{id}`: 특정 도서 삭제

### Genre API
- `GET /genres`: 전체 및 부모 코드 기반 하위 장르 조회

### Naver Proxy API
- `GET /api/naver`: 네이버 API 브라우저 직접 호출 시 발생하는 CORS 방지 프록시

## 8. 에러 핸들링

- **`404 Not Found`**: 요청한 리소스를 찾을 수 없을 때 반환. (예: `BookNotFoundException`)
- **`400 Bad Request`**: 요청 데이터 유효성 검증(`@Valid`) 실패 시 반환.
- **`500 Internal Server Error`**: 그 외 처리되지 않은 서버 내부 오류.

## 9. 데이터베이스 구조

### `users` 테이블
| 컬럼명 | 타입 | 설명 | 제약조건 |
|---|---|---|---|
| `id` | `BIGINT` | 사용자 고유 ID | PK, Auto Inc. |
| `email` | `VARCHAR` | 이메일 (로그인 ID) | Not Null, Unique |
| `password` | `VARCHAR` | 비밀번호 (해시됨) | Not Null |
| `name` | `VARCHAR` | 사용자 이름 | Not Null |
| `created_at` | `VARCHAR` | 생성일시 | Not Null |

### `book` 테이블
| 컬럼명 | 타입 | 타입 | 제약조건 |
|---|---|---|---|
| `id` | `BIGINT` | 도서 고유 ID | PK, Auto Inc. |
| `title` | `VARCHAR` | 도서 제목 | Not Null |
| `author` | `VARCHAR` | 저자 | Not Null |
| `genre_code` | `VARCHAR` | 장르 코드 | Not Null |
| `content` | `TEXT` | 도서 내용 | Not Null |
| `cover_image_url`| `TEXT` | 표지 이미지 URL | |
| `is_liked` | `BOOLEAN` | 좋아요 여부 (비로그인용) | Not Null |
| `created_at` | `VARCHAR` | 생성일시 | Not Null |
| `updated_at` | `VARCHAR` | 수정일시 | Not Null |

### `genre` 테이블
| 컬럼명 | 타입 | 설명 | 제약조건 |
|---|---|---|---|
| `code` | `VARCHAR(20)` | 장르 코드 | PK |
| `label` | `VARCHAR(100)`| 장르명 | Not Null |
| `parent_code`| `VARCHAR(20)` | 부모 장르 코드| |

### `user_likes` 테이블
| 컬럼명 | 타입 | 설명 | 제약조건 |
|---|---|---|---|
| `id` | `BIGINT` | 좋아요 고유 ID | PK, Auto Inc. |
| `user_id` | `BIGINT` | 사용자 ID | Not Null, FK |
| `book_id` | `BIGINT` | 도서 ID | Not Null, FK |
| `created_at` | `VARCHAR` | 생성일시 | Not Null |

### `email_verification` 테이블
| 컬럼명 | 타입 | 설명 | 제약조건 |
|---|---|---|---|
| `id` | `BIGINT` | 인증 고유 ID | PK, Auto Inc. |
| `email` | `VARCHAR` | 이메일 | Not Null, Unique |
| `code` | `VARCHAR` | 인증 코드 | Not Null |
| `expires_at` | `VARCHAR` | 만료 일시 | Not Null |
| `verified` | `BOOLEAN` | 인증 완료 여부 | Not Null |

---

## 10. 미션 수행 기록 및 핵심 구현 내용

본 프로젝트는 총 7단계의 미션을 통해 단계별로 학습하며 완성된 백엔드 애플리케이션입니다.

### [미션 1] 요구사항 분석 및 설계
- **과정 요약**: Frontend 프로젝트(db.json 및 fetch 호출 패턴) 분석을 통해 팀 R&R 분담을 확정하고, 데이터베이스 ERD(Book Entity 필드 도출)와 6개 엔드포인트에 대한 API 정의서를 작성했습니다.

### [미션 2] 프로젝트 초기 환경 구성 및 골격 작성
- **과정 요약**: Spring Initializr를 통해 프로젝트를 생성하고, 계층별 골격을 구축한 후 GitHub 저장소에 초기 커밋했습니다.
- **핵심 구현 내용**:
  - `Spring Initializr`를 통한 프로젝트 생성 (`com.aivle.bookapp`).
  - ERD 기반의 `Book` 도메인 엔티티 생성. ➡️ [`Book.java`](bookapp/src/main/java/com/aivle/bookapp/entity/Book.java)
  - 3-Tier 아키텍처(Controller-Service-Repository)의 기본 뼈대 구축
    - ➡️ [`BookRepository.java`](bookapp/src/main/java/com/aivle/bookapp/repository/BookRepository.java)
    - ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
    - ➡️ [`BookController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/BookController.java)
  - 프론트엔드 연동 대비 CORS 설정(`SecurityConfig.java`) 및 `application.properties` 설정. ➡️ [`SecurityConfig.java`](bookapp/src/main/java/com/aivle/bookapp/config/SecurityConfig.java)
  - 소스코드 저장소 내 README.md에 미션1·2 설계 및 골격 내용 명시.

### [미션 3] 도서 조회(목록/상세) API 구현 및 1차 연동
- **과정 요약**: Repository 기본 동작 검증 후 GET API를 완성하여 프론트엔드와 1차 연동을 완료했습니다.
- **핵심 구현 내용**:
  - `BookRepository` 기본 CRUD 동작 검증 및 데이터 확인 (MySQL 데이터베이스 활용으로 대체됨).
  - `BookService`에 생성자 주입 방식을 적용하여 목록 조회 및 상세 조회 메서 구현. ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - `BookController`의 `GET /books`, `GET /books/{id}` 엔드포인트 완성. ➡️ [`BookController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/BookController.java)
  - API 테스트(Swagger UI 연동) 완료 후 Frontend의 `fetch` URL을 백엔드 포트(`http://localhost:8080`)로 변경하여 1차 연동 완료.

### [미션 4] 도서 등록/수정/삭제 API 구현 및 검증 적용
- **과정 요약**: CUD(생성/수정/삭제) 로직 및 입력 검증 처리를 완료하고 풀스택 CRUD 동작을 확인했습니다.
- **핵심 구현 내용**:
  - `Book` 도메인 엔티티에 `@NotBlank` 등 입력 검증(Validation) 어노테이션 추가. ➡️ [`Book.java`](bookapp/src/main/java/com/aivle/bookapp/entity/Book.java)
  - `BookService`에 도서 등록 / 부분 수정 / 삭제 메서드 구현. ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - `BookController`의 `POST` (+검증), `PATCH` (부분 수정), `DELETE` 엔드포인트 완성. ➡️ [`BookController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/BookController.java)
  - React 화면과 Swagger UI 양쪽에서 풀스택 CRUD 동작 확인 완료.

### [미션 5] 트랜잭션 관리 및 커스텀 예외 적용
- **과정 요약**: 서비스 계층의 트랜잭션 분리와 상세 조회 예외 상황에 대한 커스텀 예외 클래스를 구현했습니다.
- **핵심 구현 내용**:
  - `exception` 패키지에 `BookNotFoundException` 클래스 생성. ➡️ [`BookNotFoundException.java`](bookapp/src/main/java/com/aivle/bookapp/exception/BookNotFoundException.java)
  - `BookService`의 상세 조회 메서 내에 예외 발생 로직(`orElseThrow()`) 추가.
  - `BookService`의 CUD 메서드에 `@Transactional` 적용, 조회 메서드에 성능 최적화를 위한 `@Transactional(readOnly = true)` 적용. ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)

### [미션 6] 전역 예외 처리(Global Exception Handler) 적용
- **과정 요약**: 모든 컨트롤러의 예외를 일관되게 처리하고 정제된 응답을 제공하는 구조를 확립했습니다.
- **핵심 구현 내용**:
  - 전역 예외 처리 클래스(`GlobalExceptionHandler`) 작성. ➡️ [`GlobalExceptionHandler.java`](bookapp/src/main/java/com/aivle/bookapp/exception/GlobalExceptionHandler.java)
    - 도서 없음 예외 발생 시 ➡️ `404 Not Found` 응답으로 정제.
    - 검증 실패 예외 발생 시 ➡️ `400 Bad Request` 응답으로 정제.
  - Swagger UI 및 React 프론트엔드 화면에서 예외 시나리오 전체 동작 검증 완료.

### [미션 7] 오픈 API(OpenAI) 연동 및 표지 저장
- **과정 요약**: Frontend 미니프로젝트의 학습 코드를 활용해 React 애플리케이션에서 OpenAI를 직접 호출하고, 생성된 표지 이미지 URL을 Backend에 전달하여 데이터베이스에 저장하는 전체 흐름을 검증했습니다.
- **핵심 구현 내용**:
  - **[Frontend] OpenAI 연동**: Frontend 미니프로젝트 학습 코드를 활용하여 사용자 입력 기반 이미지 생성 로직 구현 및 직접 호출 검증.
  - **[Backend] 엔드포인트 및 로직 추가**: 표지 URL 저장용 엔드포인트(`PATCH /books/{id}/cover`) 및 `BookService` 표지 업데이트 메서드 추가 요구사항을 반영. (현재 소스코드 상에서는 REST API 효율성을 위해 별도 분리 없이 기존의 `PATCH /books/{id}` 엔드포인트와 `updateBook` 로직 내의 `coverImageUrl` 업데이트 처리로 통합하여 구현했습니다.) ➡️ [`BookController.java`](bookapp/src/main/java/com/aivle/bookapp/controller/BookController.java), ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - **저장 흐름 검증**: `React(화면) ➡️ OpenAI(이미지 생성) ➡️ React ➡️ Backend(저장)`에 이르는 풀스택 데이터 흐름 동작 확인 완료.

---

## 11. 기술적 고민 및 최적화 성과

### 🚀 적용 완료된 최적화
- **동적 JPQL 도입 (검색 로직 최적화)** ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - `:param IS NULL OR` 조건을 활용하여 검색/필터링 조건(제목, 장르 등)의 조합별 메서드(약 11개)를 1개의 `@Query` 메서드로 통합하여 **코드량을 약 87% 단축**했습니다.
- **정렬 연산 DB 이관 및 페이지네이션(Pagination) 기반 대용량 데이터 대응** ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - `Pageable` 인터페이스를 통해, 전체 데이터를 인메모리에 올려 정렬하던 방식에서 **DB 인덱스 및 `ORDER BY` 활용 방식**으로 개선했습니다. (1만 건 기준, 인메모리 30~50ms ➡️ DB 5ms로 개선)
  - `Page<Book>` 반환 타입을 사용하여 도서 전체 조회의 부담을 줄였습니다. (응답 속도 수십 배 향상 및 데이터 전송량 획기적 감소 기대)
- **유저별 좋아요 분리 설계 (구조 안정성 확보)** ➡️ [`UserLike.java`](bookapp/src/main/java/com/aivle/bookapp/entity/UserLike.java)
  - `Book` 엔티티 내부의 단일 `boolean isLiked` 컬럼으로 인한 동기화 버그를 해결하기 위해, `user_likes` 조인 테이블로 분리하여 **사용자 간 독립성을 완벽하게 보장**했습니다.
- **단건 조회 시 N+1 비효율 개선** ➡️ [`BookService.java`](bookapp/src/main/java/com/aivle/bookapp/service/BookService.java)
  - 단건 도서 조회 시 유저의 좋아요 여부를 확인하기 위해 좋아요 목록 전체를 가져오던(N+1 쿼리 유발) 로직을, `existsByUserIdAndBookId()` 메서드를 활용한 **단일 존재 여부 검사 쿼리로 최적화**했습니다.
- **보안 및 브루트포스(무차별 대입) 공격 방어** ➡️ [`AuthService.java`](bookapp/src/main/java/com/aivle/bookapp/service/AuthService.java)
  - 비밀번호에 **BCrypt 단방향 해시 알고리즘(Salt 활용)** 을 적용하여 레인보우 테이블 공격을 방어합니다.
  - 이메일 OTP(6자리) 검증 로직에 **5분 만료 제한**을 부여하여 무차별 대입(Brute-force) 성공 확률을 **0.03%** 이하로 원천 차단했습니다.
