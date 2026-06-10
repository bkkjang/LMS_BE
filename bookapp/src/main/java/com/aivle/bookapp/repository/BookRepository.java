package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContaining(String title, Sort sort);
    List<Book> findByGenreCode(String genreCode, Sort sort);
    List<Book> findByGenreCodeStartingWith(String genreCode, Sort sort);
    List<Book> findByIsLikedTrue(Sort sort);

    List<Book> findByTitleContainingAndIsLikedTrue(String title, Sort sort);
    List<Book> findByGenreCodeAndIsLikedTrue(String genreCode, Sort sort);
    List<Book> findByGenreCodeStartingWithAndIsLikedTrue(String genreCode, Sort sort);

    List<Book> findByTitleContainingAndGenreCode(String title, String genreCode, Sort sort);
    List<Book> findByTitleContainingAndGenreCodeStartingWith(String title, String genreCode, Sort sort);
    List<Book> findByTitleContainingAndGenreCodeAndIsLikedTrue(String title, String genreCode, Sort sort);
    List<Book> findByTitleContainingAndGenreCodeStartingWithAndIsLikedTrue(String title, String genreCode, Sort sort);
}
