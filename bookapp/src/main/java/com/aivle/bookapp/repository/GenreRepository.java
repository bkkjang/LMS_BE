package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, String> {

    // 대분류만 (parentCode=null)
    List<Genre> findByParentCodeIsNull();

    // 특정 상위 장르의 세부 장르 (parentCode=NV)
    List<Genre> findByParentCode(String parentCode);
}
