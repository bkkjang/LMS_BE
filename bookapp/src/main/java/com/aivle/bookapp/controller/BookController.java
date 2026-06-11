package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getBooks(
            @RequestParam(required = false) String title_like,
            @RequestParam(required = false) String genreCode,
            @RequestParam(required = false) String genreCode_like,
            @RequestParam(required = false) Boolean isLiked,
            @RequestParam(required = false, defaultValue = "createdAt") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order
    ) {
        return bookService.getBooks(title_like, genreCode, genreCode_like, isLiked, _sort, _order, currentUserEmail());
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
    private String currentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof String email) {
            return email;
        }
        return null;
    }
}
