package com.aivle.bookapp.service;

import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<Genre> getGenres(String parentCode) {
        // TODO: 구현
        return null;
    }
}



