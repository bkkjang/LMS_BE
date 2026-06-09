package com.aivle.bookapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * ════════════════════════════════════════════════════════════════════════════
 *  전역 예외 처리기 — 어디서 터진 예외든 여기로 모아 '일관된 에러 응답'으로 변환하는 곳.
 * ════════════════════════════════════════════════════════════════════════════
 *
 *  [흐름] 컨트롤러/서비스에서 예외가 던져지면 → 스프링이 그 예외를 여기로 가져옴 → 여기서
 *         "어떤 예외 → 어떤 상태코드·메시지로" 변환해 응답.
 *
 *  [왜 모아두나] 이게 없으면 모든 에러가 뭉뚱그려 500(서버오류)으로 나가서, 프론트는
 *  "내가 잘못 보낸 건지(400)/없는 걸 찾은 건지(404)/서버가 진짜 터진 건지(500)"를 구분 못 합니다.
 *  한곳에 모으면 응답이 일관되고, 컨트롤러는 본업(요청 처리)에만 집중할 수 있습니다.
 *
 *  ▶ 처음 보는 것:
 *     @RestControllerAdvice : "모든 @RestController에 공통 적용되는 예외 처리 담당"이라는 표시.
 *     @ExceptionHandler(X.class) : "X 예외가 나면 이 메서드가 처리한다"는 연결.
 *     ProblemDetail : 에러 응답의 '표준 형식'(RFC 9457). { status, detail, ... } JSON으로 나가
 *                     클라이언트가 에러를 일관되게 해석하기 좋습니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 우리가 만든 BookNotFoundException → HTTP 404. "그런 자원은 없다"는 의미라 404가 정확. */
    @ExceptionHandler(BookNotFoundException.class)
    public ProblemDetail handleNotFound(BookNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /** 잘못된 인자(예: 존재하지 않는 장르 코드) → HTTP 400. "요청 자체가 잘못됐다"는 뜻. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 입력 검증 실패(@Valid가 DTO의 @NotBlank 등을 어겼을 때) → HTTP 400.
     * 어긴 필드들의 메시지를 모아 "title: 제목은 필수입니다, author: ..." 형태로 알려줍니다.
     * (아래는 '어긴 필드 목록'을 하나씩(map) "필드명: 메시지" 문자열로 바꿔 콤마로 이어붙이는(join) 코드)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
    }
}
