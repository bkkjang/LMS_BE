package com.aivle.bookapp.controller;

import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET /books
    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String title_like,
            @RequestParam(required = false) String genreCode,
            @RequestParam(required = false) String genreCode_like,
            @RequestParam(required = false) Boolean isLiked,
            @RequestParam(required = false, defaultValue = "createdAt") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order
    ) {
        // TODO: 2일차 미션 3 구현
        return null;
    }

    // GET /books/{id}
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        // TODO: 2일차 미션 3 구현
        return null;
    }

    // POST /books
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        // TODO: 2일차 미션 4 구현
        return null;
    }

    // PATCH /books/{id}
    @PatchMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        // TODO: 2일차 미션 4 구현
        return null;
    }

    // DELETE /books/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        // TODO: 2일차 미션 4 구현
    }
}
