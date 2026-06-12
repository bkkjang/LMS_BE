package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 검색·필터를 동적 쿼리 하나로 처리한다. (기존의 findBy... 조합 메서드 11개를 대체)
     *
     * 핵심 트릭: (:param IS NULL OR 조건)
     *   - 파라미터가 null이면 그 줄은 통째로 '참'이 되어 조건에서 무시된다.
     *   - 덕분에 "제목만 / 장르만 / 둘 다 / 아무것도 안 줌" 등 모든 조합을 메서드 하나로 처리한다.
     *
     * 장르는 두 가지를 모두 지원한다(기존 API 계약 유지):
     *   - genreCode   : 정확히 일치          (기존 findByGenreCode)
     *   - genrePrefix : 접두어 매칭(NV% 등)   (기존 findByGenreCodeStartingWith)
     *
     * CAST(:x AS string) : 일부 DB가 타입 미정 null을 엉뚱하게 추론해 함수가 깨지는 것을 막는다.
     *   MySQL에서도 cast(... as char)로 번역되어 안전하다.
     *
     * ※ isLiked 조건은 제거됨 — 좋아요는 user_likes 테이블로 분리되어 유저별 관리
     */
    @Query("""
            SELECT b FROM Book b
            WHERE (:title       IS NULL OR b.title LIKE CONCAT('%', CAST(:title AS string), '%'))
              AND (:genreCode   IS NULL OR b.genreCode = :genreCode)
              AND (:genrePrefix IS NULL OR b.genreCode LIKE CONCAT(CAST(:genrePrefix AS string), '%'))
            """)
    // Pageable 에 page·size·sort 가 모두 포함 — 10,000건 전체 조회 대신 20건씩 분할 반환
    Page<Book> search(@Param("title") String title,
                      @Param("genreCode") String genreCode,     // 장르 정확 일치
                      @Param("genrePrefix") String genrePrefix, // 장르 접두어 매칭
                      Pageable pageable);

    // 사용자가 좋아요한 도서 ID 목록으로 조회 (유저 책장 필터링용)
    List<Book> findByIdIn(Collection<Long> ids, Sort sort);
}
