package com.aivle.bookapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// JWT 토큰 생성 / 파싱 / 서명 검증을 담당하는 유틸 클래스
@Component
public class JwtUtil {

    // application.yml 의 jwt.secret 값을 주입받는다 (Base64 인코딩된 문자열)
    @Value("${jwt.secret}")
    private String secret;

    // application.yml 의 jwt.expiration 값 (밀리초 단위, 현재 86400000 = 24시간)
    @Value("${jwt.expiration}")
    private long expiration;

    // 로그인 성공 시 이메일을 담아 JWT 토큰을 생성한다
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)                                           // 토큰의 주인 (이메일을 식별자로 사용)
                .issuedAt(new Date())                                     // 발급 시각
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시각
                .signWith(getKey())                                       // HMAC-SHA 비밀키로 서명
                .compact();                                               // 최종 토큰 문자열 반환
    }

    // 토큰에서 이메일(subject)을 꺼낸다 — 서명 검증 실패/만료 시 예외 발생
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getKey())       // 서명을 같은 키로 검증
                .build()
                .parseSignedClaims(token)   // 파싱 + 검증 (실패 시 JwtException)
                .getPayload()
                .getSubject();              // subject 에 저장해둔 이메일 반환
    }

    // Base64 로 인코딩된 secret 문자열을 실제 HMAC 키 객체로 변환
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
