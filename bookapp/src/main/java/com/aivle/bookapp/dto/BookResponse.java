package com.aivle.bookapp.dto;

import com.aivle.bookapp.entity.Book;
import lombok.Getter;

@Getter
public class BookResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final String genreCode;
    private final String content;
    private final String coverImageUrl;
    private final Boolean isLiked;   // 현재 요청 유저 기준 (비로그인 시 false)
    private final String createdAt;
    private final String updatedAt;

    public BookResponse(Book book, boolean isLiked) {
        this.id            = book.getId();
        this.title         = book.getTitle();
        this.author        = book.getAuthor();
        this.genreCode     = book.getGenreCode();
        this.content       = book.getContent();
        this.coverImageUrl = book.getCoverImageUrl();
        this.isLiked       = isLiked;
        this.createdAt     = book.getCreatedAt();
        this.updatedAt     = book.getUpdatedAt();
    }
}
