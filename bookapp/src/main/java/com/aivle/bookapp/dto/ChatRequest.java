package com.aivle.bookapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OpenAI Chat Completions 요청 본문 (취향 추천용).
 * 이름 있는 DTO여야 OpenAPI 스키마로 잡혀 codegen이 chat_Body 타입을 생성한다.
 */
@Getter
@NoArgsConstructor
public class ChatRequest {

    private String model;
    private List<Message> messages;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    @Getter
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class ResponseFormat {
        private String type;
    }
}
