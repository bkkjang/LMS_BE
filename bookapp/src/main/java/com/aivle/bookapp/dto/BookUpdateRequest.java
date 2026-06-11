package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookUpdateRequest {

    private String title;
    private String author;
    private String genreCode;
    private String content;
    private String coverImageUrl;
}
