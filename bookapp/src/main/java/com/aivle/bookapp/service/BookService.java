package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  BookService = 도서의 '업무 규칙(비즈니스 로직)'이 사는 곳. 백엔드의 '두뇌'입니다.
 *  ※ 'DI(의존성 주입)' '@Transactional' 'Optional'이 처음 나오는 곳이라 기초부터 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 계층 분리 — 코드를 3층으로 나누는 이유
 *       Controller(접수처)  →  Service(사무실=두뇌)  →  Repository(창고)
 *       바깥 요청 받기          판단·규칙 처리           DB에서 데이터 꺼내기
 *    각 층이 한 가지 일만 하면, 바뀔 때 그 층만 고치면 되고 테스트도 쉽습니다.
 *    (컨트롤러에 DB코드+규칙을 다 때려넣으면 금방 스파게티가 됩니다.) = 계층형 아키텍처, 백엔드 정석.
 *
 *  ▶ @Service : "이 클래스는 서비스 부품"이라는 표시. 스프링이 시작할 때 자동으로 만들어 보관합니다.
 *               (스프링이 관리하는 이런 객체를 'Bean(빈)'이라 부릅니다.)
 *
 *  ▶ @RequiredArgsConstructor + '의존성 주입(DI)' — 처음이니 그림으로:
 *
 *       이 서비스는 일하려면 Repository(창고 담당)가 필요합니다. 그런데 직접 new로 안 만듭니다.
 *
 *         private final BookRepository bookRepository;   ← "나 이거 필요해" 선언만 함
 *                                  │
 *                                  ▼  (스프링이 시작할 때)
 *         스프링이 알맞은 BookRepository 객체를 '생성자를 통해' 자동으로 꽂아줌(주입)
 *
 *       - @RequiredArgsConstructor(Lombok) : final 필드들을 받는 생성자를 자동으로 만들어 줍니다.
 *       - final : "한 번 주입되면 안 바뀜"(const 느낌). 이게 붙어야 위 자동 생성자에 포함됩니다.
 *       - 왜 이렇게? 부품끼리 직접 new로 엮으면 단단히 붙어 교체·테스트가 어렵습니다. "필요한 걸 밖에서
 *         받아 쓰는" 방식이 느슨하게 연결돼 유연합니다. (React에서 props/Context로 받아 쓰는 결과 비슷)
 *
 *  ▶ @Transactional : '트랜잭션'으로 묶기.
 *       트랜잭션 = "여러 DB 작업을 하나의 단위로 묶어, 전부 성공 아니면 전부 취소(rollback)"를 보장.
 *       클래스에 readOnly=true로 '읽기 전용' 기본값을 걸고(약간의 최적화), 데이터를 '바꾸는' 메서드에만
 *       메서드 위에 @Transactional을 다시 붙여 '쓰기 가능'으로 덮어씁니다.
 * ════════════════════════════════════════════════════════════════════════════
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    // ↓ 이 두 줄이 "내가 일하려면 이 두 창구가 필요해"라는 선언. 스프링이 자동으로 꽂아줍니다(위 DI 설명).
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;   // 등록/수정 시 "존재하는 장르 코드인지" 검증용

    /**
     * 정렬을 허용할 필드 '화이트리스트(허용 목록)'.
     *  [왜?] 정렬 기준 필드명을 프론트가 문자열로 보내는데, 그걸 그대로 쓰면 엉뚱하거나 위험한 값이
     *  들어올 수 있습니다. "이 목록에 있는 필드만 허용"하면 안전합니다.
     *  - static final : 모든 객체가 공유하는, 안 바뀌는 상수.   Set = '중복 없는 모음'.
     */
    private static final Set<String> SORTABLE = Set.of("id", "title", "author", "createdAt", "updatedAt");

    // ── 목록(검색·필터·정렬) ──────────────────────────────────────────────
    /**
     * @param search 제목 부분 검색어(없으면 검색 안 함)
     * @param genre  장르 코드 접두어("NV"=소설 전체, "NV-01"=로맨스만). 없으면 필터 안 함
     * @param liked  true면 좋아요한 책만. null이면 필터 안 함
     */
    public List<BookResponse> getBooks(String search, String genre, Boolean liked, String sort, String order) {
        // 빈 문자열/공백은 "조건 없음(null)"으로 정규화 → 저장소의 'null이면 무시' 트릭이 동작하게 함.
        String s = StringUtils.hasText(search) ? search.trim() : null;
        String g = StringUtils.hasText(genre) ? genre.trim() : null;
        //   위 ? : 는 '삼항 연산자'. (조건 ? 참일때 : 거짓일때) — JS와 동일.

        // 흐름: 저장소에서 엔티티 목록 조회 → 각 엔티티를 DTO로 변환 → 목록으로 모음
        return bookRepository.search(s, g, liked, buildSort(sort, order)).stream() // .stream() : 목록을 하나씩 흐르게
                .map(BookResponse::from)   // 각 Book을 BookResponse로 변환 (JS 배열의 .map과 같은 개념)
                .toList();                 // 다시 목록(List)으로 모음
    }

    /** 외부 입력으로 Sort(정렬 기준)를 안전하게 조립. 허용 목록에 없으면 createdAt으로, asc 아니면 desc로. */
    private Sort buildSort(String sort, String order) {   // private = 이 클래스 안에서만 쓰는 보조 메서드
        String field = (sort != null && SORTABLE.contains(sort)) ? sort : "createdAt";
        Sort.Direction dir = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, field);
    }

    // ── 단건 조회 ─────────────────────────────────────────────────────────
    public BookResponse getBook(Long id) {
        return BookResponse.from(findOrThrow(id));
    }

    // ── 등록 ──────────────────────────────────────────────────────────────
    @Transactional   // 데이터를 바꾸므로 '쓰기 트랜잭션'. 정상 종료 시 확정(commit), 도중 예외 시 전체 취소.
    public BookResponse createBook(BookCreateRequest req) {
        validateGenre(req.genreCode());     // 없는 장르로는 등록 못 하게 막음
        Book book = new Book(req.title(), req.author(), req.content(), req.genreCode(), req.coverImageUrl());
        return BookResponse.from(bookRepository.save(book));   // save 후 id/시각이 채워진 객체를 반환
    }

    // ── 부분 수정(PATCH) ──────────────────────────────────────────────────
    /**
     * 보내온 필드만 반영합니다(null인 필드는 '안 보냄'으로 보고 안 건드림).
     *
     *  ▶ JPA의 '변경 감지(dirty checking)' — 왜 save()를 다시 안 불러도 저장되나?
     *    트랜잭션 안에서 조회한 엔티티는 JPA가 계속 지켜봅니다. 그 객체의 값을 바꾸면, 트랜잭션이
     *    끝날 때 JPA가 "어, 값이 바뀌었네" 하고 자동으로 UPDATE를 날립니다. 그래서 changeXxx()로
     *    값만 바꿔두면 됩니다. (놀랍고 편한 JPA의 특징)
     */
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest req) {
        Book book = findOrThrow(id);   // 없으면 여기서 예외 → 404
        if (req.title() != null)         book.changeTitle(req.title());      // 보낸 필드만 반영
        if (req.author() != null)        book.changeAuthor(req.author());
        if (req.content() != null)       book.changeContent(req.content());
        if (req.coverImageUrl() != null) book.changeCoverImageUrl(req.coverImageUrl());
        if (req.isLiked() != null)       book.changeLiked(req.isLiked());
        if (req.genreCode() != null) {
            validateGenre(req.genreCode());     // 장르를 바꾸는 경우에도 유효성 검사
            book.changeGenreCode(req.genreCode());
        }
        return BookResponse.from(book);   // 별도 save() 없음 — 위 '변경 감지'로 자동 반영됨
    }

    // ── 삭제 ──────────────────────────────────────────────────────────────
    @Transactional
    public void deleteBook(Long id) {     // void = 돌려줄 값 없음
        bookRepository.delete(findOrThrow(id));   // 존재 확인 후 삭제(없으면 404)
    }

    // ── 내부 공통 헬퍼 ────────────────────────────────────────────────────
    /**
     * id로 책을 찾고, 없으면 예외를 던지는 공통 로직. 조회·수정·삭제가 똑같이 쓰므로 한곳으로 모음(중복 제거).
     *
     *  ▶ 처음 보는 것: Optional
     *    findById는 결과를 'Optional'로 감싸 돌려줍니다 — "값이 있을 수도/없을 수도"를 타입으로 표현한 상자.
     *    (JS의 Book|null 을 안전하게 다루는 장치. null을 깜빡하고 쓰다 터지는 사고를 막아줌.)
     *    .orElseThrow(...) = "있으면 꺼내고, 없으면 괄호 안의 예외를 던져라".
     *    () -> new BookNotFoundException(id) 는 '람다'(JS의 화살표 함수와 동일): "없을 때 실행할 코드".
     */
    private Book findOrThrow(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    /** 존재하지 않는 장르 코드로의 등록/수정을 막음. 던진 예외는 전역 처리기에서 400으로 변환됨. */
    private void validateGenre(String genreCode) {
        if (!genreRepository.existsById(genreCode)) {     // ! = 부정(NOT). "존재하지 '않으면'"
            throw new IllegalArgumentException("존재하지 않는 장르 코드입니다: " + genreCode);
        }
    }
}
