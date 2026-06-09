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

    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String title_like,
            @RequestParam(required = false) String genreCode,
            @RequestParam(required = false) String genreCode_like,
            @RequestParam(required = false) Boolean isLiked,
            @RequestParam(required = false, defaultValue = "createdAt") String _sort,
            @RequestParam(required = false, defaultValue = "desc") String _order
    ) {
        return bookService.getBooks(title_like, genreCode, genreCode_like, isLiked, _sort, _order);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PatchMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return bookService.updateBook(id, fields);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
