package com.aivle.bookapp.service;

import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Book> getBooks(String titleLike, String genreCode, String genreCodeLike,
                               Boolean isLiked, String sort, String order) {
        // TODO: 2일차 미션 3 구현
        return null;
    }

    @Transactional(readOnly = true)
    public Book getBook(Long id) {
        // TODO: 2일차 미션 3 구현
        return null;
    }

    @Transactional
    public Book createBook(Book book) {
        // TODO: 2일차 미션 4 구현
        return null;
    }

    @Transactional
    public Book updateBook(Long id, Map<String, Object> fields) {
        // TODO: 2일차 미션 4 구현
        return null;
    }

    @Transactional
    public void deleteBook(Long id) {
        // TODO: 2일차 미션 4 구현
    }
}
