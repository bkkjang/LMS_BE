package com.aivle.bookapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * BookCreateRequest = 새 책을 등록할 때 프론트가 '보내오는' 데이터 모양 (요청용 DTO).
 *   POST /api/books 의 요청 본문(JSON)이 이 record로 자동 변환되어 들어옵니다.
 *   (DTO·record 기초 설명은 BookResponse.java 참고)
 *
 *  ▶ 여기 처음 나오는 것: '입력 검증(validation)'
 *    잘못된 데이터(제목 빈 값 등)는 '가장 바깥(입구)'에서 막는 게 정석입니다. 깊숙이 들어간 뒤
 *    터지면 원인 찾기가 어렵거든요. 필드에 @NotBlank 같은 '규칙 표시'를 달아두고, 컨트롤러에서
 *    @Valid 로 검사하면, 규칙을 어긴 요청은 서비스 로직에 닿기도 전에 자동으로 400(잘못된 요청)으로 거절됩니다.
 *    (프론트에서 폼 검증을 하듯, 백엔드도 "사용자 입력은 절대 믿지 말고 한 번 더 검증"하는 게 안전합니다.)
 *
 *      @NotBlank : null도, 빈 문자열("")도, 공백("  ")도 안 됨 → '필수 입력' 항목에 사용
 *      @Size     : 글자 수 범위 제한
 *      message   : 규칙을 어겼을 때 사용자에게 보여줄 안내 문구
 */
public record BookCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
        String title,

        @NotBlank(message = "저자는 필수입니다.")
        @Size(max = 100, message = "저자는 100자 이하여야 합니다.")
        String author,

        @NotBlank(message = "내용은 필수입니다.")   // 내용은 AI 표지 생성 프롬프트의 바탕이라 필수로 둠
        String content,

        @NotBlank(message = "장르 코드는 필수입니다.")
        String genreCode,

        // coverImageUrl엔 검증을 안 걸었음: 표지는 등록 후 AI로 생성·저장할 수 있어 처음엔 비어도 되기 때문(선택).
        String coverImageUrl
) {
}
