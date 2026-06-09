package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 제목 부분 일치 (title_like)
    List<Book> findByTitleContaining(String title);

    // 하위 장르 정확 일치 (genreCode=NV-01)
    List<Book> findByGenreCode(String genreCode);

    // 상위 장르 전방 일치 (genreCode_like=NV)
    List<Book> findByGenreCodeStartingWith(String genreCode);

    // 좋아요만 (isLiked=true)
    List<Book> findByIsLikedTrue();

    // 제목 검색 + 좋아요 필터
    List<Book> findByTitleContainingAndIsLikedTrue(String title);

    // 하위 장르 + 좋아요 필터
    List<Book> findByGenreCodeAndIsLikedTrue(String genreCode);

    // 상위 장르 + 좋아요 필터
    List<Book> findByGenreCodeStartingWithAndIsLikedTrue(String genreCode);

    // 제목 검색 + 하위 장르
    List<Book> findByTitleContainingAndGenreCode(String title, String genreCode);

    // 제목 검색 + 상위 장르
    List<Book> findByTitleContainingAndGenreCodeStartingWith(String title, String genreCode);

    // 제목 검색 + 하위 장르 + 좋아요
    List<Book> findByTitleContainingAndGenreCodeAndIsLikedTrue(String title, String genreCode);

    // 제목 검색 + 상위 장르 + 좋아요
    List<Book> findByTitleContainingAndGenreCodeStartingWithAndIsLikedTrue(String title, String genreCode);
}
