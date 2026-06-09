package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 장르 저장소. (interface·extends·제네릭<>·"본문 없이 동작하는 마법" 설명은 BookRepository.java 참고)
 * JpaRepository<Genre, String> → Genre를 다루고, 그 기본키(@Id code)가 String이라 둘째 칸이 String.
 *
 *  ▶ 처음 보는 것: '쿼리 메서드' — 메서드 '이름'만으로 쿼리를 자동 생성
 *    아래 두 메서드는 본문이 없는데도, 스프링이 '메서드 이름의 규칙'을 해석해 알아서 쿼리를 만들어 줍니다.
 *       findByParentCodeIsNull()   →  "parentCode가 비어있는(NULL) 장르 찾기"  (= 최상위/대분류)
 *       findByParentCode(code)     →  "parentCode가 주어진 값인 장르 찾기"      (= 특정 대분류의 하위들)
 *    규칙: findBy + 필드명 + 조건(IsNull 등). 이름만 정확히 지으면 SQL을 한 줄도 안 써도 됩니다.
 *    (List<Genre> = "Genre들이 여러 개 담긴 목록". List는 JS의 배열과 비슷한 '순서 있는 모음')
 */
public interface GenreRepository extends JpaRepository<Genre, String> {

    List<Genre> findByParentCodeIsNull();

    List<Genre> findByParentCode(String parentCode);
}
