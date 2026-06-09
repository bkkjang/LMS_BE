package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  BookController = 바깥(브라우저/프론트)의 HTTP 요청을 '가장 먼저 받는 접수처'.
 *  ※ '컨트롤러'와 각종 매핑 어노테이션이 처음 나오는 곳이라 기초부터 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  React Router가 URL을 컴포넌트에 연결하듯, 여기선 'URL + HTTP방식'을 '메서드'에 연결합니다.
 *  실제 일 처리는 직접 안 하고 Service(두뇌)에 넘깁니다 — 컨트롤러는 얇게 유지(정석).
 *
 *  ▶ @RestController : "이 클래스는 데이터(JSON)를 주는 API 컨트롤러".
 *      메서드가 돌려준 값(BookResponse 등)을 스프링이 '자동으로 JSON으로 변환'해 응답에 실어 보냅니다.
 *      (HTML 화면을 주는 게 아니라 데이터를 주는 서버라 'Rest')
 *  ▶ @RequestMapping("/api/books") : 이 클래스 안 모든 메서드의 '공통 주소 앞부분'.
 *      '/api'는 "이건 화면이 아니라 데이터용 통로"임을 구분하는 흔한 관례.
 *
 *  [URL + 방식  →  메서드  매핑표]
 *     GET    /api/books        →  getBooks()     목록(검색/필터/정렬)
 *     GET    /api/books/{id}   →  getBook()      단건
 *     POST   /api/books        →  createBook()   등록 (성공 201)
 *     PATCH  /api/books/{id}   →  updateBook()   부분 수정
 *     DELETE /api/books/{id}   →  deleteBook()   삭제 (성공 204)
 * ════════════════════════════════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor   // BookService를 생성자 주입으로 받음 (DI 설명은 BookService.java 참고)
public class BookController {

    private final BookService bookService;

    /**
     * GET /api/books — 목록 조회 (+검색/필터/정렬)
     *  @GetMapping : 'HTTP GET 요청'을 이 메서드에 연결. (GET = 조회. 데이터를 바꾸지 않는 읽기)
     *  @RequestParam : URL 뒤 쿼리스트링(?key=value)을 메서드 인자로 받음.
     *      예) /api/books?search=별&genre=NV&sort=title&order=asc
     *      required=false → 없어도 됨(없으면 null).   defaultValue → 없을 때 쓸 기본값.
     */
    @GetMapping
    public List<BookResponse> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean liked,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order
    ) {
        return bookService.getBooks(search, genre, liked, sort, order);   // 접수만 하고 처리는 서비스에 위임
    }

    /**
     * GET /api/books/{id} — 단건 조회
     *  @PathVariable : 주소 경로 안의 {id} 부분을 꺼내 인자로 받음. 예) /api/books/3 → id=3
     */
    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    /**
     * POST /api/books — 새 책 등록
     *  @RequestBody : 요청 본문(JSON)을 BookCreateRequest 객체로 자동 변환해 받음.
     *  @Valid       : 그 DTO에 달아둔 검증 규칙(@NotBlank 등)을 실제로 검사하라는 지시.
     *                 위반 시 서비스로 가기 전에 자동으로 400(전역 처리기가 메시지 정리).
     *
     *  ▶ ResponseEntity로 감싼 이유 = 상태코드를 201로 지정하려고.
     *    HTTP 상태코드(서버가 결과를 알리는 표준 숫자):
     *       200 성공 · 201 새로 생성됨 · 204 성공(돌려줄 본문 없음) · 400 잘못된 요청 · 404 없음 · 500 서버오류
     *    '생성 성공'의 표준은 200이 아니라 201이라, 직접 201로 지정합니다.
     */
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookCreateRequest request) {
        BookResponse created = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);   // CREATED = 201
    }

    /**
     * PATCH /api/books/{id} — 부분 수정 (좋아요 토글·표지 저장·폼 수정이 전부 이 하나로 처리)
     * 여기엔 @Valid를 안 붙임 — 부분 수정이라 '일부 필드만 옴'이 정상이라서.
     */
    @PatchMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @RequestBody BookUpdateRequest request) {
        return bookService.updateBook(id, request);
    }

    /**
     * DELETE /api/books/{id} — 삭제. 성공 시 본문 없이 204(No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {   // Void = '돌려줄 본문 타입이 없음'
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();   // noContent() = 204
    }
}
