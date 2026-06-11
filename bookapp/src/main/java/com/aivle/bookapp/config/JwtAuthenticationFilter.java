package com.aivle.bookapp.config;

import com.aivle.bookapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// 모든 HTTP 요청에 대해 JWT 토큰을 검사하는 필터
// OncePerRequestFilter : 요청당 딱 한 번만 실행됨을 보장
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 값을 꺼낸다
        // 형식: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            // "Bearer " 이후 실제 토큰 문자열만 추출 (앞 7자리 제거)
            String token = header.substring(7);
            try {
                // 토큰에서 이메일 추출 (내부에서 서명 검증 + 만료 확인)
                String email = jwtUtil.extractEmail(token);

                // 인증 객체 생성 — 이메일을 principal 로, 권한 목록은 빈 리스트
                // 컨트롤러에서 SecurityContextHolder.getContext().getAuthentication().getPrincipal() 로 꺼낼 수 있다
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, List.of());

                // SecurityContext 에 저장 — 이후 Spring Security 가 "인증된 사용자"로 인식
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // 토큰이 위조되었거나 만료된 경우 — 인증 정보를 저장하지 않고 그냥 통과
                // 이후 SecurityConfig 에서 해당 엔드포인트가 authenticated() 이면 401 반환
            }
        }

        // 다음 필터(또는 컨트롤러)로 요청을 넘긴다
        chain.doFilter(request, response);
    }
}
