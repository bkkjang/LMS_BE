package com.aivle.bookapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "email_verification")
@Getter @Setter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(name = "expires_at", nullable = false)
    private String expiresAt;

    @Column(nullable = false)
    private boolean verified = false;

    public static EmailVerification of(String email, String code) {
        EmailVerification ev = new EmailVerification();
        ev.setEmail(email);
        ev.setCode(code);
        ev.setExpiresAt(Instant.now().plusSeconds(300).toString()); // 5분
        return ev;
    }
}
