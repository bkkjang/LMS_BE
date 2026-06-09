package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  BookRepository = "DB에 실제로 들어가 저장/조회/삭제를 수행하는 창구"
 *  ※ 'interface'와 'Repository'가 처음 나오는 곳이라 기초부터 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 처음 보는 것 1: 'class'가 아니라 'interface' 네?
 *    interface(인터페이스)는 "이런 메서드가 있다"는 '약속(목록)'만 적고, 실제 동작 코드는 없는 틀입니다.
 *    (TS의 interface 와 비슷한 느낌.) 보통은 누군가 이 약속을 '구현'해야 하는데 — 우리는 구현을 안 짭니다.
 *
 *  ▶ 처음 보는 것 2: 본문이 거의 비었는데 어떻게 동작하나? (스프링의 마법)
 *       interface BookRepository extends JpaRepository<Book, Long>
 *                                  └────────── 이 한 줄만으로 ──────────┘
 *    JpaRepository를 '상속(extends)'만 하면, 스프링이 실행 시점에 '구현체를 자동 생성'해 끼워줍니다.
 *    그래서 아래 메서드들이 코드 한 줄 없이 공짜로 따라옵니다:
 *       findAll()      → 전체 조회
 *       findById(id)   → id로 한 건 조회
 *       save(book)     → 저장(없으면 INSERT, 있으면 UPDATE)
 *       deleteById(id) / count() / existsById(id) ...
 *
 *  ▶ 처음 보는 것 3: JpaRepository<Book, Long> 의 < > 두 칸 = '제네릭(generic)'
 *       <Book, Long>  →  "이 저장소는 Book 엔티티를 다루고, 그 기본키(@Id) 타입은 Long이다"
 *       (TS의 Array<Book> 처럼, 다룰 타입을 < > 안에 지정하는 문법입니다.)
 *
 *  아래는 기본 제공으로는 부족한 '검색/필터'를 우리가 직접 정의한 부분입니다. ↓
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 왜 검색 메서드를 '하나'로 합쳤나 — '조합 폭발' 제거
 *    검색어·장르·좋아요를 각각 켜고 끌 수 있으면 경우의 수가 곱해집니다(검색만/장르만/둘다/…).
 *    메서드 이름으로 다 만들면 십수 개가 필요하죠. 그래서 '조건을 동적으로 켜고 끄는' 쿼리 하나로 처리합니다.
 *
 *    핵심 트릭:  (:파라미터 IS NULL OR 조건)
 *       → 파라미터가 null이면 그 줄은 통째로 '참'이 되어 무시됩니다.
 *       → 덕분에 '검색만 / 장르만 / 둘 다 / 아무것도 안 줌'을 단 하나의 쿼리로 다 처리.
 *
 *    CAST(:search AS string)를 쓴 이유: 일부 DB(특히 PostgreSQL)는 타입이 안 정해진 null을 엉뚱한
 *    타입으로 추론해 함수가 깨집니다("function lower(bytea) does not exist"). CAST로 "이건 문자열"이라고
 *    못 박으면 어떤 DB에서도 안전합니다(H2 테스트·Postgres 운영 모두 OK).
 *
 *    ※ @Query 안의 문장은 SQL이 아니라 'JPQL' — '테이블'이 아니라 '엔티티(Book)'를 대상으로 하는
 *      자바용 질의어입니다. 그래서 book 테이블이 아니라 Book, b.title처럼 '객체 기준'으로 씁니다.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            SELECT b FROM Book b
            WHERE (:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
              AND (:genre  IS NULL OR b.genreCode LIKE CONCAT(CAST(:genre AS string), '%'))
              AND (:liked  IS NULL OR b.liked = :liked)
            """)
    List<Book> search(@Param("search") String search,   // @Param : 위 쿼리의 :search 자리에 이 값을 꽂음
                      @Param("genre") String genre,      //          :genre 자리
                      @Param("liked") Boolean liked,      //          :liked 자리 (null 가능하도록 대문자 Boolean)
                      Sort sort);   // 정렬 기준은 Service에서 만들어 넘기면 ORDER BY가 자동으로 붙습니다.
}
