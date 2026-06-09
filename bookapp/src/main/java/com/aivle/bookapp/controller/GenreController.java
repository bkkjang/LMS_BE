package com.aivle.bookapp.controller;

import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    // GET /genres
    // GET /genres?parentCode=null
    // GET /genres?parentCode=NV
    @GetMapping
    public List<Genre> getGenres(@RequestParam(required = false) String parentCode) {
        // TODO: 구현
        return null;
    }
}
