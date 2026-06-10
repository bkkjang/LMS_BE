package com.aivle.bookapp.service;

import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.repository.GenreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<Genre> getGenres(String parentCode) {
        if ("null".equals(parentCode)) return genreRepository.findByParentCodeIsNull();
        if (parentCode != null)        return genreRepository.findByParentCode(parentCode);
        return genreRepository.findAll();
    }
}
