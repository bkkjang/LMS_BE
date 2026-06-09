package com.aivle.bookapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  Genre = "장르 한 개" 를 나타내는 '엔티티(Entity)'
 *  ※ 이 파일은 우리 프로젝트에서 '엔티티'와 'JPA'가 처음 나오는 곳이라, 기초부터 천천히 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 먼저 'JPA'가 뭔가요? (몰라도 됩니다, 지금 알면 됩니다)
 *    - 원래 DB를 다루려면 "SELECT * FROM genre WHERE ..." 같은 SQL을 손으로 써야 합니다.
 *    - JPA는 "자바 객체 ↔ DB 테이블을 어떻게 연결할지" 정해둔 자바 표준 '규약(약속)' 입니다.
 *    - 우리가 이 클래스에 @Entity, @Id, @Column 같은 표를 붙여 두면, JPA가 그걸 보고 SQL을
 *      대신 만들어 줍니다. 덕분에 우리는 SQL 없이 "객체를 저장/조회"하는 느낌으로 DB를 씁니다.
 *    - 이렇게 객체와 테이블을 이어주는 걸 'ORM' 이라 부르고, JPA를 실제로 구현한 라이브러리가
 *      'Hibernate' 입니다. (우리는 Hibernate를 직접 안 부르고, 스프링이 알아서 씁니다.)
 *    → 한 줄 요약: JPA = "자바 객체와 DB 테이블 사이의 자동 통역사" (프론트엔 없는 개념).
 *
 *  ▶ '엔티티'란? : DB 테이블의 한 줄(row)과 짝을 이루는 자바 객체.
 *      genre 테이블의 한 줄  ↔  Genre 객체 하나
 *
 *  ▶ ⭐ 가장 헷갈리는 것: 세로로 쓴 자바 필드  ↔  가로로 생기는 DB 컬럼
 *
 *     Genre 클래스 (자바: 세로)         genre 테이블 (DB: 가로로 펼쳐짐)
 *     ────────────────────────         ──────────────────────────────────
 *     @Id  code        ───────▶        code         컬럼  (기본키 / PK)
 *          label       ───────▶        label        컬럼
 *          parentCode  ───────▶        parent_code  컬럼
 *
 *     → 세로 필드 1개 = 가로 컬럼 1개.   그리고 'Genre 객체 하나' = '테이블의 한 줄(행)'.
 *
 *  ▶ 실제 데이터 예시 (한 줄 = 객체 하나 = 테이블의 한 행)
 *     code="NV"     label="소설"    parent_code=null    ← 최상위(대분류): 부모가 없어 null(빈 값)
 *     code="NV-01"  label="로맨스"  parent_code="NV"    ← '소설'의 하위(소분류)
 *     code="NV-02"  label="판타지"  parent_code="NV"
 * ════════════════════════════════════════════════════════════════════════════
 */

// ▼ 여기서부터 클래스 위에 붙은 '어노테이션(@표시)' 들입니다. JS의 데코레이터(@)와 비슷합니다.
@Entity                                  // JPA에게: "이 클래스는 DB 테이블과 연결되는 엔티티야"
@Table(name = "genre")                   // 연결될 테이블 이름은 genre (안 적으면 클래스명이 테이블명)
@Getter                                  // Lombok이 getCode()·getLabel() 같은 '값 꺼내는 메서드'를 자동 생성
                                         //   (Lombok = 반복 코드를 대신 만들어주는 도구. 아래 private 설명과 한 쌍)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // '빈 생성자'를 자동 생성하되 외부 사용은 막음(아래 설명)
public class Genre {
    // ↑ public class Genre : '클래스(설계도)' 하나를 정의.
    //   - class  : 데이터(필드) + 동작(메서드)을 묶는 설계도. (JS의 class와 거의 같음)
    //   - public : 어디서든 이 클래스를 쓸 수 있게 '공개'. (파일명 Genre.java 와 이름이 같아야 함)

    /**
     * 첫 번째 필드 'code'. 여기서 '필드'와 'private'와 '타입'을 처음 만나니 자세히 봅니다.
     *
     *  ▶ '필드(field)' : 클래스가 가지는 데이터 한 칸. (JS 객체의 속성/프로퍼티와 같음)
     *
     *  ▶ 'private String code;' 를 한 단어씩 뜯으면:
     *      private → '접근 제어'. 이 필드는 클래스 '바깥'에서 직접 못 건드림(숨김).
     *      String  → '타입'. 이 칸엔 '문자열'만 들어감. (자바는 타입을 반드시 적습니다 = 정적 타입)
     *      code    → 필드(칸)의 이름.
     *
     *  ▶ ❓ "왜 private을 써야 하나요?"
     *      private이 없으면 어디서든 genre.code = "아무거나"  로 값을 막 바꿀 수 있습니다.
     *      그러면 누가 언제 바꿨는지 추적이 안 되고, 잘못된 값이 들어가기 쉽습니다.
     *      그래서 필드는 private으로 '숨기고', 꼭 필요한 접근만 '정해진 통로(메서드)'로 엽니다.
     *      이걸 '캡슐화(encapsulation)' 라 하고, 자바에서 필드는 거의 항상 private이 정석입니다.
     *
     *           바깥 코드 ──✗── (직접 접근 차단)         private String code;   ← 숨김
     *           바깥 코드 ──✓──▶ getCode() 로 읽기  ◀── @Getter 가 만들어 준 통로
     *
     *      (JS로 치면 클래스의 #private 필드 + getter 를 두는 것과 같은 결입니다.)
     */
    @Id                  // 이 필드가 테이블의 '기본키(primary key)' = 각 줄을 구분하는 고유값. 장르는 코드가
                         //   이미 고유하므로 코드 자체를 기본키로 씀(자연 키). (책은 숫자 id를 자동 발급받음)
    @Column(length = 20) // 이 필드가 매핑될 '컬럼' 설정. length=20 → 최대 20글자.
    private String code;

    @Column(nullable = false, length = 100)   // nullable=false → 이 칸은 '비울 수 없음(필수)'. (NOT NULL)
    private String label;                      // 사람이 읽는 장르 이름. 예: "로맨스"

    /**
     * 상위 장르 코드. 최상위(대분류)는 부모가 없어 null(빈 값)이 허용됩니다.
     *   @Column(name = "parent_code", ...) ← name 으로 실제 컬럼 이름을 지정합니다. 왜?
     *      자바는 parentCode (카멜표기), DB는 parent_code (스네이크표기) 가 관례라 이름을 맞춰주는 것.
     * 일부러 Genre 객체 참조가 아니라 단순 String으로 둔 이유: 프론트는 부모 '코드 문자열'만 필요로 하고,
     * 객체끼리 연결(자기참조 관계)하면 복잡도만 커지기 때문입니다. (필요한 만큼만 단순하게 = 좋은 설계)
     */
    @Column(name = "parent_code", length = 20)
    private String parentCode;

    /**
     * '생성자(constructor)' : 이 클래스로 객체를 만들 때( new Genre(...) ) 호출되는 특별한 메서드.
     *  값을 받아 위 필드들을 채웁니다. 이렇게 '필요한 값을 한 번에 받는 생성자'를 두면,
     *  항상 완전한 상태의 객체만 생기도록 강제할 수 있습니다.
     *
     *  ※ 위쪽 @NoArgsConstructor로 만든 '빈 생성자'는 JPA가 DB에서 읽어온 값으로 객체를 만들 때
     *    내부적으로 필요해서 둔 것인데, 아무나 'new Genre()'로 빈 껍데기를 못 만들게 PROTECTED로
     *    잠가뒀습니다. 그래서 우리가 코드에서 새 장르를 만들 땐 '이' 생성자를 씁니다.
     */
    public Genre(String code, String label, String parentCode) {
        this.code = code;            // this.code = '이 객체의' code 필드. (JS의 this.code 와 동일)
        this.label = label;          //   왼쪽 this.code = 필드, 오른쪽 code = 매개변수(받은 값). 이름이 같아 this로 구분.
        this.parentCode = parentCode;
    }
}
