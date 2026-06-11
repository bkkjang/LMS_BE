package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.*;
import com.aivle.bookapp.entity.EmailVerification;
import com.aivle.bookapp.entity.User;
import com.aivle.bookapp.repository.EmailVerificationRepository;
import com.aivle.bookapp.repository.UserRepository;
import com.aivle.bookapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // [1단계] 이메일로 OTP 발송
    // 이미 가입된 이메일이면 거부, 아니면 6자리 코드 생성 후 DB 저장 + 이메일 발송
    @Transactional
    public String sendCode(SendCodeRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String code = generateCode();

        // 같은 이메일로 이미 인증 요청이 있으면 재사용 (코드/만료시간만 갱신)
        EmailVerification ev = emailVerificationRepository.findByEmail(req.getEmail())
                .orElse(EmailVerification.of(req.getEmail(), code));

        ev.setCode(code);
        ev.setExpiresAt(Instant.now().plusSeconds(300).toString()); // 5분 유효
        ev.setVerified(false);
        emailVerificationRepository.save(ev);

        emailService.sendVerificationCode(req.getEmail(), code);
        return "인증 코드를 이메일로 발송했습니다.";
    }

    // [2단계] OTP 코드 확인
    // 코드가 맞고 만료되지 않았으면 verified = true 로 변경
    @Transactional
    public String checkCode(CheckCodeRequest req) {
        EmailVerification ev = emailVerificationRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("인증 코드를 먼저 발송해주세요."));

        if (!req.getCode().equals(ev.getCode())) {
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");
        }
        if (Instant.now().isAfter(Instant.parse(ev.getExpiresAt()))) {
            throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
        }

        ev.setVerified(true);
        emailVerificationRepository.save(ev);
        return "이메일 인증이 완료되었습니다.";
    }

    // [3단계] 회원가입
    // 이메일 인증이 완료된 상태에서만 가입 허용
    // 비밀번호는 BCrypt 로 해시하여 저장 — 평문 저장 금지
    // 가입 완료 후 email_verification 레코드 삭제 (임시 데이터 정리)
    // 가입 즉시 JWT 발급하여 바로 로그인 상태로 만들어 줌
    @Transactional
    public AuthResponse signup(SignupRequest req) {
        EmailVerification ev = emailVerificationRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 인증을 먼저 완료해주세요."));

        if (!ev.isVerified()) {
            throw new IllegalArgumentException("이메일 인증을 먼저 완료해주세요.");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // BCrypt 해시 저장
        user.setName(req.getName());
        userRepository.save(user);

        emailVerificationRepository.delete(ev); // 임시 인증 레코드 삭제

        // 회원가입 완료 즉시 JWT 발급 (별도 로그인 불필요)
        return new AuthResponse(jwtUtil.generateToken(req.getEmail()), req.getEmail(), req.getName());
    }

    // 로그인
    // DB에서 이메일로 유저를 찾고, BCrypt matches() 로 비밀번호 비교
    // 성공 시 JWT 발급
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // BCrypt 는 단방향 해시 — 저장된 해시와 입력값을 비교할 때 matches() 사용
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getEmail()), user.getEmail(), user.getName());
    }

    // 6자리 랜덤 숫자 OTP 생성 (000000 ~ 999999)
    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
