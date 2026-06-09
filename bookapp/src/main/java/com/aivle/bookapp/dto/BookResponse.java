package com.aivle.bookapp.dto;

import com.aivle.bookapp.entity.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BookResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final String genreCode;
    private final String content;
    private final String coverImageUrl;
    @JsonProperty("isLiked")
    private final boolean isLiked;
    private final String createdAt;
    private final String updatedAt;

    public BookResponse(Book book) {
        this.id           = book.getId();
        this.title        = book.getTitle();
        this.author       = book.getAuthor();
        this.genreCode    = book.getGenreCode();
        this.content      = book.getContent();
        this.coverImageUrl = book.getCoverImageUrl();
        this.isLiked      = book.isLiked();
        this.createdAt    = book.getCreatedAt();
        this.updatedAt    = book.getUpdatedAt();
    }
}
