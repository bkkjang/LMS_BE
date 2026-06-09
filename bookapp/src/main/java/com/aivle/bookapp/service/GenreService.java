package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.GenreResponse;
import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 장르 조회 로직. (@Service·DI·@Transactional·.stream().map() 설명은 BookService.java 주석 참고)
 *
 *  parentCode 하나로 세 가지 조회를 처리합니다:
 *    - 안 줌(null)    → 전체 장르
 *    - "null"(문자열) → 최상위(대분류)만   (프론트가 ?parentCode=null 로 호출하는 경우)
 *    - 그 외 코드      → 해당 대분류의 하위(소분류)들
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreResponse> getGenres(String parentCode) {
        List<Genre> genres;                                  // 먼저 빈 변수를 선언하고,
        if (parentCode == null) {                            // 조건에 따라 알맞은 조회 결과를 담습니다.
            genres = genreRepository.findAll();
        } else if ("null".equalsIgnoreCase(parentCode)) {
            genres = genreRepository.findByParentCodeIsNull();
        } else {
            genres = genreRepository.findByParentCode(parentCode);
        }
        return genres.stream().map(GenreResponse::from).toList();   // 엔티티 목록 → DTO 목록으로 변환
    }
}
