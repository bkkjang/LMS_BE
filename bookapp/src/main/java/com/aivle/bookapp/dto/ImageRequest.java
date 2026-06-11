package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OpenAI Images 생성 요청 본문 (표지 생성용).
 * 이름 있는 DTO여야 OpenAPI 스키마로 잡혀 codegen이 image_Body 타입을 생성한다.
 */
@Getter
@NoArgsConstructor
public class ImageRequest {

    private String model;
    private String prompt;
    private Integer n;
    private String size;
    private String quality;
}
