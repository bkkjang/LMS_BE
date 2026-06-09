# LMS_BE

KT AIVLE School 5기 미니프로젝트 — 도서관 관리 시스템 백엔드

## 기술 스택

- Java 17
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- MySQL 8
- Lombok
- Springdoc OpenAPI (Swagger UI)

## 실행 방법

### 1. MySQL 데이터베이스 준비

MySQL이 로컬에 설치되어 있어야 합니다.

- host: `localhost`
- port: `3306`
- username: `root`
- password: `aivle`

> `bookdb` 데이터베이스는 앱 실행 시 자동 생성됩니다.

### 2. 서버 실행

```bash
cd bookapp
./gradlew bootRun
```

서버가 시작되면 초기 데이터(장르 63개, 도서 51권)가 자동으로 로드됩니다.

### 3. Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

## API 엔드포인트

### 도서

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/books` | 도서 목록 조회 (필터/정렬) |
| GET | `/books/{id}` | 도서 상세 조회 |
| POST | `/books` | 도서 등록 |
| PATCH | `/books/{id}` | 도서 수정 |
| PATCH | `/books/{id}/like` | 좋아요 토글 |
| DELETE | `/books/{id}` | 도서 삭제 |

#### 쿼리 파라미터 (`GET /books`)

| 파라미터 | 설명 | 예시 |
|---------|------|------|
| `title_like` | 제목 검색 | `?title_like=별` |
| `genreCode` | 세부 장르 필터 | `?genreCode=NV-01` |
| `genreCode_like` | 대분류 장르 필터 | `?genreCode_like=NV` |
| `isLiked` | 좋아요 필터 | `?isLiked=true` |
| `_sort` | 정렬 기준 | `?_sort=title` |
| `_order` | 정렬 방향 | `?_order=asc` |

### 장르

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/genres` | 전체 장르 조회 |
| GET | `/genres?parentCode=null` | 대분류만 조회 |
| GET | `/genres?parentCode=NV` | 특정 대분류의 세부 장르 조회 |
