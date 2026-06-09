package com.aivle.bookapp.entity;

import jakarta.persistence.*;   // * = 이 패키지의 여러 클래스를 한꺼번에 import (@Entity, @Id, @Column …)
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  Book = "책 한 권" 엔티티. 이 서비스의 가장 핵심 데이터입니다.
 *  ※ '엔티티/JPA/필드/private/캡슐화' 같은 기초는 Genre.java 주석에 자세히 적어두었으니 먼저 보세요.
 *    여기서는 Book에만 새로 나오는 것 — ①숫자 id 자동발급 ②시각 자동기록 ③boolean — 을 설명합니다.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 세로 자바 필드  ↔  가로 book 테이블 컬럼  (Genre보다 컬럼이 많을 뿐, 원리는 똑같습니다)
 *     id             ───▶  id              (기본키/PK, DB가 1,2,3… 자동 발급)
 *     title          ───▶  title
 *     author         ───▶  author
 *     genreCode      ───▶  genre_code      (자바 카멜 → DB 스네이크 이름으로 매핑)
 *     content        ───▶  content         (TEXT: 길이 제한 없는 큰 글)
 *     coverImageUrl  ───▶  cover_image_url
 *     liked          ───▶  is_liked        (★ 자바필드 liked ↔ DB컬럼 is_liked ↔ JSON isLiked — 셋이 다름!)
 *     createdAt      ───▶  created_at      (직접 안 넣음 — 저장될 때 자동 기록)
 *     updatedAt      ───▶  updated_at      (직접 안 넣음 — 수정될 때 자동 갱신)
 * ════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA 전용 빈 생성자(외부 차단). 설명은 Genre.java 참고.
// @EntityListeners(...) : "이 엔티티가 저장/수정될 때 'Auditing(자동 기록)' 장치가 끼어들게" 연결하는 표시.
//   이게 있어야 아래 @CreatedDate / @LastModifiedDate 가 실제로 동작합니다. (config/JpaConfig 와 한 쌍)
@EntityListeners(AuditingEntityListener.class)
public class Book {

    /**
     * 기본키 id. 장르(코드가 곧 고유값)와 달리, 책은 의미 있는 고유 코드가 없어 '그냥 번호'를 자동 발급받습니다.
     *   - Long : 자바의 '정수' 타입(아주 큰 수까지). JS는 number 하나지만, 자바는 타입을 구체적으로 정합니다.
     *            (정수는 int/Long, 소수는 double, 참/거짓은 boolean, 문자열은 String …)
     *   - @GeneratedValue(IDENTITY) : 번호 발급을 DB에 맡김(PostgreSQL이 1, 2, 3… 자동 증가).
     *            우리가 id를 직접 안 정해도, 저장할 때 DB가 알아서 채웁니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)        // nullable=false → 비울 수 없음(필수). (이 옵션 설명은 Genre.java 참고)
    private String title;            // 제목

    @Column(nullable = false)
    private String author;           // 저자

    @Column(name = "genre_code", nullable = false, length = 20)
    private String genreCode;        // 이 책의 장르 코드. 예: "NV-01". genre 테이블의 code와 의미상 연결.

    /**
     * 줄거리/내용. columnDefinition = "TEXT" 로 '길이 제한 없는 큰 문자열' 컬럼으로 지정합니다.
     * (지정 안 하면 보통 최대 255자로 잡혀 긴 글이 잘립니다.)
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;    // AI가 생성한 표지(Data URL). 등록 시점엔 비어 있을 수 있어 nullable.

    /**
     * '좋아요(내 책장)' 여부.
     *   - boolean : 참(true)/거짓(false) 두 값만 갖는 타입.
     *   - 이름이 셋 다 다른 점에 주의(위 표의 ★): 자바 필드는 liked, DB 컬럼은 is_liked, API JSON은 isLiked.
     *     자바 필드명은 짧게(liked), 컬럼은 @Column(name="is_liked")로, JSON은 DTO(BookResponse)에서 isLiked로 맞춥니다.
     */
    @Column(name = "is_liked", nullable = false)
    private boolean liked;

    /**
     * 생성/수정 시각 — 우리가 직접 넣지 않습니다. 'Auditing'이 자동으로 채워줍니다.
     *   - Instant : '시간의 한 순간'을 UTC(세계표준시) 기준으로 표현하는 타입.
     *     왜 String이 아니라 Instant? 시각을 문자열로 저장하면 정렬·비교·시간대 계산에서 사고가 잦습니다.
     *     시각은 '시간 전용 타입'으로 다루는 게 정석.
     */
    @CreatedDate                                     // 행이 '처음 저장될 때' 현재 시각을 자동 기록
    @Column(name = "created_at", updatable = false)  // updatable=false → 이후 수정 때 이 값은 안 건드림
    private Instant createdAt;

    @LastModifiedDate                                // 행이 '수정될 때마다' 현재 시각으로 자동 갱신
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 새 책 생성용 생성자. id/시각은 자동으로 채워지므로 받지 않고, 좋아요는 새 책이면 항상 false로 시작.
     * (생성자·this 설명은 Genre.java 참고)
     */
    public Book(String title, String author, String content, String genreCode, String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.genreCode = genreCode;
        this.coverImageUrl = coverImageUrl;
        this.liked = false;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  [캡슐화] 값을 바꾸는 메서드 — 아무 setter나 열지 않고 '의도가 드러나는 이름'으로만 변경을 허용
    //
    //  왜 setTitle 대신 changeTitle 같은 메서드만 둘까? (Genre.java의 private 설명과 이어집니다)
    //   - 필드는 private이라 바깥에서 직접 book.title = ... 못 함.
    //   - 대신 "무엇을 위한 변경인지" 이름에 담은 메서드로만 열어두면, 값이 어디서 바뀌는지 추적이 쉽고
    //     안전합니다. (객체가 스스로 자기 상태를 책임지게 하는 것 = 객체지향 정석)
    //
    //  메서드 문법: public void changeTitle(String title) { ... }
    //   - public : 외부(서비스)에서 호출하니 공개
    //   - void   : 돌려주는 값이 없음(그냥 바꾸기만)
    //   - (String title) : 바꿀 새 값을 매개변수로 받음
    // ────────────────────────────────────────────────────────────────────────
    public void changeLiked(boolean liked)        { this.liked = liked; }
    public void changeCoverImageUrl(String url)   { this.coverImageUrl = url; }
    public void changeTitle(String title)         { this.title = title; }
    public void changeAuthor(String author)       { this.author = author; }
    public void changeContent(String content)     { this.content = content; }
    public void changeGenreCode(String genreCode) { this.genreCode = genreCode; }
}
