package com.aivle.bookapp.dto;

import com.aivle.bookapp.entity.Genre;

/**
 * GenreResponse = 장르를 프론트로 내보낼 때의 모양 (응답용 DTO).
 * 엔티티(Genre)를 직접 노출하지 않고 필요한 필드만 내보냅니다.
 * (DTO를 왜 쓰는지, record가 뭔지, from이 뭔지는 BookResponse.java 주석에 자세히 설명했습니다.)
 */
public record GenreResponse(
        String code,
        String label,
        String parentCode
) {
    /** 엔티티 → DTO 변환기. (from 패턴 설명은 BookResponse.java 참고) */
    public static GenreResponse from(Genre g) {
        return new GenreResponse(g.getCode(), g.getLabel(), g.getParentCode());
    }
}
