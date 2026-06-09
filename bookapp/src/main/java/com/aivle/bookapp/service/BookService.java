package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks(String titleLike, String genreCode, String genreCodeLike,
                                       Boolean isLiked, String sort, String order) {
        Sort s = buildSort(sort, order);
        boolean liked = Boolean.TRUE.equals(isLiked);
        List<Book> books;

        if (titleLike != null && genreCode != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndGenreCodeAndIsLikedTrue(titleLike, genreCode, s)
                    : bookRepository.findByTitleContainingAndGenreCode(titleLike, genreCode, s);
        } else if (titleLike != null && genreCodeLike != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndGenreCodeStartingWithAndIsLikedTrue(titleLike, genreCodeLike, s)
                    : bookRepository.findByTitleContainingAndGenreCodeStartingWith(titleLike, genreCodeLike, s);
        } else if (titleLike != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndIsLikedTrue(titleLike, s)
                    : bookRepository.findByTitleContaining(titleLike, s);
        } else if (genreCode != null) {
            books = liked
                    ? bookRepository.findByGenreCodeAndIsLikedTrue(genreCode, s)
                    : bookRepository.findByGenreCode(genreCode, s);
        } else if (genreCodeLike != null) {
            books = liked
                    ? bookRepository.findByGenreCodeStartingWithAndIsLikedTrue(genreCodeLike, s)
                    : bookRepository.findByGenreCodeStartingWith(genreCodeLike, s);
        } else if (liked) {
            books = bookRepository.findByIsLikedTrue(s);
        } else {
            books = bookRepository.findAll(s);
        }

        return books.stream().map(BookResponse::new).toList();
    }

    private Sort buildSort(String sort, String order) {
        String field = switch (sort != null ? sort : "createdAt") {
            case "title"     -> "title";
            case "author"    -> "author";
            case "updatedAt" -> "updatedAt";
            default          -> "createdAt";
        };
        Sort.Direction direction = "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, field);
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long id) {
        return new BookResponse(bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id)));
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest req) {
        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setGenreCode(req.getGenreCode());
        book.setContent(req.getContent());
        book.setCoverImageUrl(req.getCoverImageUrl());
        book.setLiked(Boolean.TRUE.equals(req.getIsLiked()));
        return new BookResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (req.getTitle()         != null) book.setTitle(req.getTitle());
        if (req.getAuthor()        != null) book.setAuthor(req.getAuthor());
        if (req.getGenreCode()     != null) book.setGenreCode(req.getGenreCode());
        if (req.getContent()       != null) book.setContent(req.getContent());
        if (req.getCoverImageUrl() != null) book.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getIsLiked()       != null) book.setLiked(req.getIsLiked());

        return new BookResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse toggleLike(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setLiked(!book.isLiked());
        return new BookResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new BookNotFoundException(id);
        bookRepository.deleteById(id);
    }
}
