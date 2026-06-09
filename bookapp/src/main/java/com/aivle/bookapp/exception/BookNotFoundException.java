package com.aivle.bookapp.exception;

/**
 * "요청한 책을 찾을 수 없음"을 나타내는 우리만의 예외(에러). 없는 id의 책을 조회/수정/삭제할 때 던집니다.
 *
 *  ▶ '예외(exception)'란? : 프로그램이 정상 진행을 못 할 때 "문제 생겼어!" 하고 위로 던지는 신호.
 *     던지면(throw) 그 자리에서 실행이 멈추고, 처리해줄 곳을 찾아 호출 단계를 거슬러 올라갑니다.
 *
 *  ▶ 왜 직접 만드나? 자바 기본 예외를 그냥 쓰면 "무슨 에러인지" 의미가 흐릿합니다. 도메인에 맞는
 *     이름의 예외를 만들면 의도가 분명하고, GlobalExceptionHandler에서 "이 예외는 404로 변환"처럼
 *     한곳에서 일관되게 처리할 수 있습니다.
 *
 *  ▶ 처음 보는 것:
 *     class ... extends RuntimeException → 'extends'는 '상속'(부모의 기능을 물려받음).
 *        RuntimeException을 상속하면 'unchecked 예외'가 되어, 강제 try-catch 없이 자연스럽게 위로
 *        전파됩니다. (서비스에서 던지면 컨트롤러를 거쳐 전역 처리기까지 알아서 올라감.)
 *     super(...) → 부모(RuntimeException)의 생성자를 호출해 에러 메시지를 전달.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found: " + id);   // "+"로 문자열과 숫자를 이어붙임 → "Book not found: 3"
    }
}
