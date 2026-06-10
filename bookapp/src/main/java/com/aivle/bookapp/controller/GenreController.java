package com.aivle.bookapp.controller;

import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    // GET /genres              → 전체
    // GET /genres?parentCode=null  → 대분류만
    // GET /genres?parentCode=NV    → NV 하위 장르
    @GetMapping
    public List<Genre> getGenres(@RequestParam(required = false) String parentCode) {
        return genreService.getGenres(parentCode);
    }
}
