package com.aivle.bookapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS 활성화 — Security 체인이 corsConfigurationSource Bean을 사용하게 한다.
            // 이게 있어야 preflight(OPTIONS)를 CorsFilter가 인증 전에 200으로 처리한다.
            .cors(Customizer.withDefaults())

            // JWT 방식은 세션/쿠키를 사용하지 않으므로 CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)

            // 세션을 아예 만들지 않는다 — JWT 자체가 상태를 담고 있으므로 서버에 세션 불필요
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 엔드포인트별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 회원가입/로그인은 토큰 없이 허용
                .requestMatchers("/auth/**").permitAll()
                // Swagger UI 도 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 도서·장르 조회(GET)는 비로그인도 허용 — 복도·전체 도서·상세 페이지
                .requestMatchers(HttpMethod.GET, "/books/**", "/genres/**").permitAll()
                // 도서 생성/수정/삭제는 로그인 필요
                .requestMatchers(HttpMethod.POST, "/books/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/books/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/books/**").authenticated()
                // 위에서 명시되지 않은 나머지 요청은 모두 로그인 필요 (Naver 프록시 등 포함)
                .anyRequest().authenticated()
            )

            // Security 레이어에서 발생하는 예외 처리
            // @RestControllerAdvice(GlobalExceptionHandler)는 필터 레이어 예외를 잡지 못하므로 여기서 별도 처리
            .exceptionHandling(ex -> ex
                // 토큰 없음 / 만료 / 위조 → 401 Unauthorized
                .authenticationEntryPoint((request, response, e) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                        new ObjectMapper().writeValueAsString(Map.of("message", "로그인이 필요합니다."))
                    );
                })
                // 토큰은 있지만 해당 리소스 접근 권한 없음 → 403 Forbidden
                .accessDeniedHandler((request, response, e) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(
                        new ObjectMapper().writeValueAsString(Map.of("message", "접근 권한이 없습니다."))
                    );
                })
            )

            // UsernamePasswordAuthenticationFilter 앞에 JWT 필터를 끼워 넣는다
            // → 모든 요청이 컨트롤러에 도달하기 전에 토큰 검사를 거침
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비밀번호를 BCrypt 해시로 암호화하는 Bean
    // AuthService 에서 회원가입 시 encode(), 로그인 시 matches() 로 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 정책 — Security 의 .cors() 가 이 Bean 을 사용한다.
    // (WebConfig 의 WebMvc CORS 와 중복되면 헤더가 두 번 붙을 수 있어 단일 소스로 통일)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));   // Authorization 헤더 포함 허용
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
