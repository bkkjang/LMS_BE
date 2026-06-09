package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.GenreResponse;
import com.aivle.bookapp.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 장르 REST API. (@RestController·매핑·@RequestParam 설명은 BookController.java 주석 참고)
 *
 *     GET /api/genres                  →  전체 장르
 *     GET /api/genres?parentCode=null  →  최상위(대분류)만
 *     GET /api/genres?parentCode=NV    →  NV(소설)의 하위(소분류)들
 */
@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreResponse> getGenres(@RequestParam(required = false) String parentCode) {
        return genreService.getGenres(parentCode);
    }
}
