package com.aivle.bookapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
@Getter @Setter
@NoArgsConstructor
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    public static UserLike of(Long userId, Long bookId) {
        UserLike ul = new UserLike();
        ul.setUserId(userId);
        ul.setBookId(bookId);
        ul.setCreatedAt(Instant.now().toString());
        return ul;
    }
}
