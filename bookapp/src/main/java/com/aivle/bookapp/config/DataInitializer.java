package com.aivle.bookapp.config;

import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.entity.Genre;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  DataInitializer = 앱이 켜질 때, 초기 데이터(책·장르)를 DB에 한 번 채워 넣는 부품.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  ▶ 왜 필요한가? 4차 프로젝트 데이터(장르 62·책 51)가 db.json에 있었습니다. 진짜 DB로 옮기려면
 *    그걸 한 번 넣어줘야 하는데, 그 일을 '서버 시작 시 자동으로' 해 줍니다.
 *    (PostgreSQL은 영구 저장이라 한 번 넣으면 재시작해도 남고, 아래 'count() > 0이면 건너뜀'으로 중복 방지.
 *     테스트용 H2는 매번 비어 시작하므로 매 실행 새로 채워집니다.)
 *
 *  ▶ 처음 보는 것:
 *    @Component : @Service/@RestController처럼 "스프링이 관리하는 부품"이라는 '범용' 표시
 *                 (서비스도 컨트롤러도 아닌 보조 도구라 일반 이름 @Component를 씀).
 *    CommandLineRunner : "앱 구동이 끝난 직후 run()을 자동으로 한 번 실행"하는 스프링 표준 장치.
 *    implements ... : 그 약속(인터페이스)을 구현하겠다는 선언 → 그래서 run()을 @Override로 채움.
 *
 *  ▶ 데이터 출처: src/main/resources/seed/initial-data.json (db.json을 복사해 백엔드에 자체 포함).
 *    백엔드가 자기 데이터를 스스로 가지면, 외부(프론트) 파일 위치에 의존하지 않아 깔끔합니다.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;   // JSON ↔ 자바 객체 변환기(스프링 부트 기본 제공)

    /** @Transactional : 아래 적재 전체를 한 단위로 묶어, 도중 실패해도 '절반만 들어가는' 사고를 막음. */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (genreRepository.count() > 0 || bookRepository.count() > 0) return;   // 이미 있으면 건너뜀(중복 방지)

        // 번들된 JSON 파일을 읽어 트리(JsonNode) 형태로 파싱.
        //  try (...) { } = 'try-with-resources': 괄호 안에서 연 파일(InputStream)을 끝나면 자동으로 닫아줌(누수 방지).
        JsonNode root;
        try (InputStream in = new ClassPathResource("seed/initial-data.json").getInputStream()) {
            root = objectMapper.readTree(in);
        }

        seedGenres(root.get("genres"));   // 장르 먼저(책이 장르 코드를 참조하므로 순서가 중요)
        seedBooks(root.get("books"));
    }

    private void seedGenres(JsonNode genres) {
        if (genres == null) return;
        for (JsonNode n : genres) {   // for-each : JS의 for...of 와 같은 반복(목록을 하나씩 n에 담아 순회)
            JsonNode parent = n.get("parentCode");
            // 최상위 장르는 parentCode가 JSON에서 null. 그 경우 자바 null로 안전 처리.
            String parentCode = (parent == null || parent.isNull()) ? null : parent.asString();
            genreRepository.save(new Genre(n.get("code").asString(), n.get("label").asString(), parentCode));
        }
    }

    private void seedBooks(JsonNode books) {
        if (books == null) return;
        for (JsonNode n : books) {
            Book book = new Book(
                    n.get("title").asString(),
                    n.get("author").asString(),
                    text(n.get("content")),
                    n.get("genreCode").asString(),
                    text(n.get("coverImageUrl"))
            );
            JsonNode liked = n.get("isLiked");
            if (liked != null && !liked.isNull() && liked.asBoolean()) {
                book.changeLiked(true);   // 새 책은 기본 false라, 좋아요였던 책만 true로 변경
            }
            bookRepository.save(book);   // createdAt/updatedAt은 Auditing이 저장 시점 기준으로 자동 기록
        }
    }

    /** JSON 필드가 없거나 null이면 자바 null을, 아니면 문자열을 돌려주는 작은 도우미. */
    private String text(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.asString();
    }
}
