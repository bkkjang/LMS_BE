package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Page<BookResponse> getBooks(
            @RequestParam(required = false) String title_like,
            @RequestParam(required = false) String genreCode,
            @RequestParam(required = false) String genreCode_like,
            @RequestParam(required = false) Boolean isLiked,
            @RequestParam(required = false, defaultValue = "createdAt") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        return bookService.getBooks(title_like, genreCode, genreCode_like, isLiked, _sort, _order, page, size, currentUserEmail());
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {
        return bookService.getBook(id, currentUserEmail());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody BookCreateRequest req) {
        return bookService.createBook(req);
    }

    @PatchMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @RequestBody BookUpdateRequest req) {
        return bookService.updateBook(id, req, currentUserEmail());
    }

    @PatchMapping("/{id}/like")
    public BookResponse toggleLike(@PathVariable Long id) {
        return bookService.toggleLike(id, currentUserEmail());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    // SecurityContext에서 현재 로그인한 유저의 이메일을 꺼낸다. 비로그인이면 null.
    // AnonymousAuthenticationToken 체크 필수 — 토큰 없는 요청도 Spring Security가
    // principal="anonymousUser" 인 익명 객체를 자동으로 넣기 때문에 null 체크만으로는 부족하다.
    private String currentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (String) auth.getPrincipal();
    }
}
