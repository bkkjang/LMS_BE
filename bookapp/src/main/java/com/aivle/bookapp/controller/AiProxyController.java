package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.ChatRequest;
import com.aivle.bookapp.dto.ImageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * OpenAI 프록시 — 브라우저에서 api.openai.com 을 직접 부르면 CORS로 막히고 API Key도 노출된다.
 * 프론트는 OpenAI 요청 본문(body)을 그대로 이 컨트롤러로 보내고, 서버가 키를 붙여 OpenAI에 전달한다.
 *  - POST /ai/chat   → https://api.openai.com/v1/chat/completions      (취향 추천)
 *  - POST /ai/image  → https://api.openai.com/v1/images/generations    (표지 생성)
 *
 * 키는 application.yml 의 openai.api-key (= 환경변수 OPENAI_API_KEY) 에서만 읽는다.
 * SecurityConfig 의 .anyRequest().authenticated() 에 포함되어 로그인 필수.
 */
@RestController
@RequestMapping("/ai")
public class AiProxyController {

    @Value("${openai.api-key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest body) throws Exception {
        return forward("https://api.openai.com/v1/chat/completions", body);
    }

    @PostMapping("/image")
    public ResponseEntity<String> image(@RequestBody ImageRequest body) throws Exception {
        return forward("https://api.openai.com/v1/images/generations", body);
    }

    private ResponseEntity<String> forward(String url, Object body) throws Exception {
        String json = objectMapper.writeValueAsString(body);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(120)) // 이미지 생성은 길어질 수 있음
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        return ResponseEntity.status(res.statusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(res.body());
    }
}
