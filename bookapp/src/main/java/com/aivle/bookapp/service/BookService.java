package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks(String titleLike, String genreCode, String genreCodeLike,
                                       Boolean isLiked, String sort, String order) {
        List<Book> books;
        boolean liked = Boolean.TRUE.equals(isLiked);

        if (titleLike != null && genreCode != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndGenreCodeAndIsLikedTrue(titleLike, genreCode)
                    : bookRepository.findByTitleContainingAndGenreCode(titleLike, genreCode);
        } else if (titleLike != null && genreCodeLike != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndGenreCodeStartingWithAndIsLikedTrue(titleLike, genreCodeLike)
                    : bookRepository.findByTitleContainingAndGenreCodeStartingWith(titleLike, genreCodeLike);
        } else if (titleLike != null) {
            books = liked
                    ? bookRepository.findByTitleContainingAndIsLikedTrue(titleLike)
                    : bookRepository.findByTitleContaining(titleLike);
        } else if (genreCode != null) {
            books = liked
                    ? bookRepository.findByGenreCodeAndIsLikedTrue(genreCode)
                    : bookRepository.findByGenreCode(genreCode);
        } else if (genreCodeLike != null) {
            books = liked
                    ? bookRepository.findByGenreCodeStartingWithAndIsLikedTrue(genreCodeLike)
                    : bookRepository.findByGenreCodeStartingWith(genreCodeLike);
        } else if (liked) {
            books = bookRepository.findByIsLikedTrue();
        } else {
            books = bookRepository.findAll();
        }

        return sortBooks(books, sort, order).stream()
                .map(BookResponse::new)
                .toList();
    }

    private List<Book> sortBooks(List<Book> books, String sort, String order) {
        Comparator<Book> comparator = switch (sort != null ? sort : "createdAt") {
            case "title"     -> Comparator.comparing(Book::getTitle, Comparator.nullsLast(Comparator.naturalOrder()));
            case "author"    -> Comparator.comparing(Book::getAuthor, Comparator.nullsLast(Comparator.naturalOrder()));
            case "updatedAt" -> Comparator.comparing(Book::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            default          -> Comparator.comparing(Book::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        };
        if ("desc".equalsIgnoreCase(order)) comparator = comparator.reversed();
        return books.stream().sorted(comparator).toList();
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
        book.setCreatedAt(req.getCreatedAt());
        book.setUpdatedAt(req.getUpdatedAt());
        return new BookResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest req) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (req.getTitle()        != null) book.setTitle(req.getTitle());
        if (req.getAuthor()       != null) book.setAuthor(req.getAuthor());
        if (req.getGenreCode()    != null) book.setGenreCode(req.getGenreCode());
        if (req.getContent()      != null) book.setContent(req.getContent());
        if (req.getCoverImageUrl()!= null) book.setCoverImageUrl(req.getCoverImageUrl());
        if (req.getIsLiked()      != null) book.setLiked(req.getIsLiked());
        if (req.getUpdatedAt()    != null) book.setUpdatedAt(req.getUpdatedAt());

        return new BookResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new BookNotFoundException(id);
        bookRepository.deleteById(id);
    }
}
