package com.aivle.bookapp.service;

import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    // 장르 데이터는 변경이 거의 없으므로 캐시 적용 — 최초 1회만 DB 조회 후 메모리에서 응답
    // parentCode 값별로 별도 캐시 엔트리가 생성된다 (null / "null" / 특정코드 각각 캐싱)
    @Cacheable(value = "genres", key = "#parentCode")
    @Transactional(readOnly = true)
    public List<Genre> getGenres(String parentCode) {
        if (parentCode == null || parentCode.isBlank()) return genreRepository.findAll();
        if ("null".equalsIgnoreCase(parentCode)) return genreRepository.findByParentCodeIsNull();
        return genreRepository.findByParentCode(parentCode);
    }
}
