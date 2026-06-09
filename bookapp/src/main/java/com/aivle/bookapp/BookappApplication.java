// ┌──────────────────────────────────────────────────────────────────────────┐
// │  이 파일은? 앱을 켜는 '시작 버튼'.  (JS로 치면 index.js / main.jsx)         │
// │  ./gradlew bootRun 을 하면 → 결국 이 파일의 main() 이 실행되어 서버가 켜짐.  │
// └──────────────────────────────────────────────────────────────────────────┘

package com.aivle.bookapp;
// ↑ package(패키지) : 이 파일이 어느 '폴더 경로'에 속하는지 적는 줄.
//   자바는 '폴더 구조 = package' 가 일치해야 합니다. (이 파일은 .../com/aivle/bookapp/ 폴더에 있음)
//   JS에서 import 경로 같은 거라 보면 됩니다 — 다른 파일이 이 클래스를 찾는 '주소'.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// ↑ import : 다른 곳에 있는 클래스를 '가져다 쓰겠다'는 선언. (JS의 import 와 똑같은 개념)

/**
 * [@SpringBootApplication 한 줄의 정체 — 스프링의 '자동 조립' 마법]
 *
 *  @ 로 시작하는 건 '어노테이션(annotation)' 입니다. JS/TS의 데코레이터(@) 와 비슷한데,
 *  "이 클래스/메서드에 이런 역할·설정을 부여해줘" 라고 표시를 붙이는 것입니다.
 *
 *  이 한 줄이 사실 세 가지를 켭니다:
 *    1) 설정의 출발점이라고 표시
 *    2) 자동 설정: 깔려 있는 라이브러리(JPA·웹 등)를 보고 "DB랑 웹서버가 필요하겠네" 하며 기본 세팅을 깔아줌
 *    3) 컴포넌트 스캔: 이 파일이 있는 패키지(com.aivle.bookapp)와 그 하위 폴더를 전부 뒤져서
 *       @Service, @RestController, @Repository 같은 표가 붙은 클래스를 자동으로 찾아 등록
 *
 *  → 그래서 우리가 만드는 모든 코드는 com.aivle.bookapp "아래"에 둬야 자동 인식됩니다.
 *  → 또 그래서 우리 코드엔 'new BookService()' 같은 게 없어요. 스프링이 다 만들어 끼워줍니다.
 *    (React의 <Context.Provider>가 값을 내려주듯, 스프링이 부품을 알아서 내려준다고 생각하세요.)
 */
@SpringBootApplication
public class BookappApplication {
    // ↑ public class BookappApplication : '클래스(class)' 하나를 정의.
    //   - class : 데이터와 동작을 묶는 '설계도'. (JS의 class 와 거의 같음)
    //   - public : '어디서든 접근 가능' 이라는 공개 범위. (반대는 private = 이 클래스 안에서만)
    //   - 자바 규칙: 파일 이름(BookappApplication.java) 과 public class 이름이 똑같아야 합니다.

    public static void main(String[] args) {
        // ↑ 자바 프로그램은 무조건 이 'main' 메서드에서 실행을 시작합니다. (정해진 약속)
        //   - public  : 외부(자바 실행기)가 호출해야 하니 공개
        //   - static  : 객체(인스턴스)를 안 만들고 바로 실행할 수 있는 메서드라는 뜻
        //   - void    : '반환값이 없다'(아무것도 돌려주지 않음)
        //   - String[] args : 실행할 때 딸려오는 옵션들(거의 안 씀). String[] = 문자열의 배열.

        SpringApplication.run(BookappApplication.class, args);
        // ↑ 이 한 줄이: 내장 웹서버(Tomcat)를 띄우고 → DB 연결을 만들고 → 우리가 짠 부품을 전부 조립해
        //   서버를 가동시킵니다. 콘솔에 "Started BookappApplication" 이 뜨면 성공입니다.
    }
}
