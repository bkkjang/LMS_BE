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

    // 1단계: 인증 코드 발송
    @Transactional
    public String sendCode(SendCodeRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String code = generateCode();
        EmailVerification ev = emailVerificationRepository.findByEmail(req.getEmail())
                .orElse(EmailVerification.of(req.getEmail(), code));

        ev.setCode(code);
        ev.setExpiresAt(Instant.now().plusSeconds(300).toString());
        ev.setVerified(false);
        emailVerificationRepository.save(ev);

        emailService.sendVerificationCode(req.getEmail(), code);
        return "인증 코드를 이메일로 발송했습니다.";
    }

    // 2단계: 코드 확인
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

    // 3단계: 회원가입
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
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setName(req.getName());
        userRepository.save(user);

        emailVerificationRepository.delete(ev);

        return new AuthResponse(jwtUtil.generateToken(req.getEmail()), req.getEmail(), req.getName());
    }

    // 로그인
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()), user.getEmail(), user.getName());
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
