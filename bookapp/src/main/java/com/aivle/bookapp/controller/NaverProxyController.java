package com.aivle.bookapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 브라우저에서 Naver API를 직접 호출하면 CORS로 막힌다.
 * Vite 프록시(/api/naver → localhost:8080)를 거쳐 이 컨트롤러에 도달하면,
 * 서버 측에서 Naver API를 호출하고 결과를 반환한다.
 *
 * 요청 예시: GET /api/naver?naverPath=v1/search/book_adv.json&d_titl=해리포터&display=10&start=1
 *  → https://openapi.naver.com/v1/search/book_adv.json?d_titl=해리포터&display=10&start=1
 *
 * SecurityConfig의 .anyRequest().authenticated() 범위에 포함되어 로그인 필수다.
 */
@RestController
@RequestMapping("/api/naver")
public class NaverProxyController {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @GetMapping
    public ResponseEntity<String> proxy(
            @RequestParam String naverPath,
            @RequestParam Map<String, String> allParams) throws Exception {

        // naverPath를 제외한 나머지 파라미터를 쿼리스트링으로 조립
        String query = allParams.entrySet().stream()
                .filter(e -> !e.getKey().equals("naverPath"))
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                        + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String url = "https://openapi.naver.com/" + naverPath + (query.isEmpty() ? "" : "?" + query);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .GET()
                .build();

        HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        return ResponseEntity.status(res.statusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(res.body());
    }
}
