package com.aivle.bookapp.repository;

import com.aivle.bookapp.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    List<UserLike> findByUserId(Long userId);
    Optional<UserLike> findByUserIdAndBookId(Long userId, Long bookId);
    // 단건 존재 여부 — getBook() 에서 전체 liked IDs 로딩 대신 사용 (N+1 제거)
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
}
