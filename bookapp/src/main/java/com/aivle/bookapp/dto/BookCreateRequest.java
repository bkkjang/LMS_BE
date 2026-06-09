package com.aivle.bookapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String genreCode;

    @NotBlank
    private String content;

    private String coverImageUrl = "";

    @JsonProperty("isLiked")
    private boolean isLiked = false;
    private String createdAt;
    private String updatedAt;
}
