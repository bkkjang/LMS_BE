package com.aivle.bookapp.dto;

import com.aivle.bookapp.entity.Book;

import java.time.Instant;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  BookResponse = 책 정보를 프론트로 '내보낼 때'의 모양 (응답용 DTO)
 *  ※ 이 파일은 'DTO'와 'record'가 처음 나오는 곳이라 기초부터 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 'DTO'가 뭔가요? (Data Transfer Object = 데이터 전송용 객체)
 *    "바깥(프론트)과 주고받을 때만 쓰는 데이터 모양" 입니다. 엔티티(Book)와 따로 둡니다.
 *
 *    [엔티티 ↔ DTO ↔ JSON 그림]
 *       Book 엔티티            BookResponse(DTO)           JSON (프론트가 받는 것)
 *       = DB 저장용 '속'  ──▶  = API 전송용 '겉'    ──▶   { "id":1, "isLiked":false, ... }
 *        (필드 liked)          (필드 isLiked)
 *
 *  ▶ 왜 엔티티(Book)를 그대로 안 주고 굳이 DTO로 바꾸나?
 *     1) API 모양 안정화: DB 구조(엔티티)가 바뀌어도 응답 모양은 그대로 유지 → 프론트가 안 깨짐
 *     2) 노출 제어: 내보낼 필드만 정확히 노출 (예: 비밀번호 같은 건 빼고)
 *     3) 직렬화 사고 예방: 엔티티를 직접 JSON으로 바꾸면 JPA 관련 함정에 빠지기 쉬움
 *     → 그래서 '속(DB)'과 '겉(API)'을 분리하고, 그 사이에 DTO라는 번역 계층을 둡니다.
 *
 *  ▶ 'record'가 뭔가요?
 *     자바 16+의 record는 "값만 담는, 못 바꾸는(불변) 객체"를 한 줄로 만드는 문법입니다.
 *     아래 괄호 안에 필드만 나열하면, 컴파일러가 생성자·getter·equals/toString을 전부 자동 생성합니다.
 *     "한 번 만들어 전달만 하는" DTO에 딱 맞아요. (TS의 readonly 타입 / 불변 객체 느낌)
 *     ※ 여기 적은 필드 이름(isLiked 등)이 '그대로 JSON 키'가 됩니다. 그래서 프론트가 기대하는 키와 일치시킴.
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        String content,
        String genreCode,
        String coverImageUrl,
        boolean isLiked,
        Instant createdAt,   // Instant는 JSON으로 나갈 때 자동으로 ISO 문자열("2026-06-09T01:23:45Z")이 됩니다.
        Instant updatedAt
) {
    /**
     * 엔티티(Book) → 응답 DTO(BookResponse) 로 바꿔주는 '변환기'.
     *   - static : 객체를 안 만들고 'BookResponse.from(book)' 처럼 클래스 이름으로 바로 호출.
     *   - 변환 규칙을 이 한곳에 모아두면, 규칙이 바뀌어도 여기만 고치면 됩니다.
     *   - 'from'은 "~로부터 만든다"는 관례적 이름.
     */
    public static BookResponse from(Book b) {
        return new BookResponse(
                b.getId(), b.getTitle(), b.getAuthor(), b.getContent(),
                b.getGenreCode(), b.getCoverImageUrl(), b.isLiked(),   // boolean 게터는 isLiked() 형태(Lombok 규칙)
                b.getCreatedAt(), b.getUpdatedAt()
        );
    }
}
