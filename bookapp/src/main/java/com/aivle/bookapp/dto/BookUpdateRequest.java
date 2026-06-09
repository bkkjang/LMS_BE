package com.aivle.bookapp.dto;

/**
 * BookUpdateRequest = 기존 책을 '부분 수정(PATCH)' 할 때 프론트가 보내오는 데이터 모양 (요청용 DTO).
 *   (DTO·record 기초 설명은 BookResponse.java 참고)
 *
 *  ▶ 왜 모든 필드가 선택(빈 값 허용)인가?
 *    PATCH는 "보낸 필드만 바꾸고, 안 보낸 건 그대로 둔다"는 부분 수정입니다. 프론트는 상황 따라 일부만 보냅니다:
 *      좋아요 토글 →  { "isLiked": true }            (이것만 옴)
 *      표지 저장   →  { "coverImageUrl": "..." }      (이것만 옴)
 *      폼 수정     →  { "title": ..., "author": ... } (여러 개 옴)
 *    안 보낸 값은 null로 들어오고, 서비스에서 "null이 아닌 것만 반영"합니다. (BookService.updateBook 참고)
 *
 *  ▶ isLiked가 기본형 boolean이 아니라 Boolean(맨 앞 대문자!)인 이유 — 헷갈리기 쉬운 자바 포인트:
 *      boolean (소문자) : 값이 없으면 자동으로 false. → "안 보냄"과 "false로 보냄"을 구분 못 함.
 *      Boolean (대문자) : null이 될 수 있는 '객체형'. → "이 필드는 아예 안 왔다(null)"를 표현 가능.
 *    부분 수정에선 "이 필드가 왔는지 안 왔는지"를 구분해야 하므로 Boolean을 씁니다.
 *    (※ 여기엔 @NotBlank 같은 필수 검증을 안 답니다. 부분 수정이라 '일부만 안 옴'이 정상이라서.)
 */
public record BookUpdateRequest(
        String title,
        String author,
        String content,
        String genreCode,
        String coverImageUrl,
        Boolean isLiked
) {
}
