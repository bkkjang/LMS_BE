package com.aivle.bookapp.service;

import com.aivle.bookapp.dto.BookCreateRequest;
import com.aivle.bookapp.dto.BookResponse;
import com.aivle.bookapp.dto.BookUpdateRequest;
import com.aivle.bookapp.entity.Book;
import com.aivle.bookapp.entity.User;
import com.aivle.bookapp.entity.UserLike;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.UserLikeRepository;
import com.aivle.bookapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> getBooks(String titleLike, String genreCode, String genreCodeLike,
                                       Boolean isLiked, String sort, String order, String userEmail) {
        // isLiked=true는 로그인한 유저의 개인 책장 기능 → 비로그인 시 401
        if (Boolean.TRUE.equals(isLiked) && userEmail == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Set<Long> likedIds = getUserLikedIds(userEmail);
        Sort sortObj = buildSort(sort, order);

        // isLiked=true → 이 유저가 좋아요한 책만 반환
        if (Boolean.TRUE.equals(isLiked)) {
            if (likedIds.isEmpty()) return List.of();

            // liked 책 목록 가져온 뒤 추가 필터(제목·장르)는 인메모리로 처리
            Stream<Book> stream = bookRepository.findByIdIn(likedIds, sortObj).stream();
            if (titleLike   != null) stream = stream.filter(b -> b.getTitle().contains(titleLike));
            if (genreCode   != null) stream = stream.filter(b -> b.getGenreCode().equals(genreCode));
            if (genreCodeLike != null) stream = stream.filter(b -> b.getGenreCode().startsWith(genreCodeLike));
            return stream.map(b -> new BookResponse(b, true)).toList();
        }

        // 일반 검색·필터 (isLiked 상태는 유저별로 표시)
        return bookRepository.search(titleLike, genreCode, genreCodeLike, sortObj)
                .stream()
                .map(b -> new BookResponse(b, likedIds.contains(b.getId())))
                .toList();
    }

    private Sort buildSort(String sort, String order) {
        String field = switch (sort != null ? sort : "createdAt") {
            case "title"     -> "title";
            case "author"    -> "author";
            case "updatedAt" -> "updatedAt";
            default          -> "createdAt";
        };
        Sort.Direction direction = "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, field);
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long id, String userEmail) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        boolean liked = getUserLikedIds(userEmail).contains(id);
        return new BookResponse(book, liked);
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest req) {
        Book book = new Book();
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setGenreCode(req.getGenreCode());
        book.setContent(req.getContent());
        book.setCoverImageUrl(req.getCoverImageUrl());
        return new BookResponse(bookRepository.save(book), false);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest req, String userEmail) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (req.getTitle()         != null) book.setTitle(req.getTitle());
        if (req.getAuthor()        != null) book.setAuthor(req.getAuthor());
        if (req.getGenreCode()     != null) book.setGenreCode(req.getGenreCode());
        if (req.getContent()       != null) book.setContent(req.getContent());
        if (req.getCoverImageUrl() != null) book.setCoverImageUrl(req.getCoverImageUrl());

        boolean liked = getUserLikedIds(userEmail).contains(id);
        return new BookResponse(bookRepository.save(book), liked);
    }

    @Transactional
    public BookResponse toggleLike(Long bookId, String userEmail) {
        if (userEmail == null) throw new IllegalArgumentException("로그인이 필요합니다.");

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Optional<UserLike> existing = userLikeRepository.findByUserIdAndBookId(user.getId(), bookId);
        boolean isNowLiked;
        if (existing.isPresent()) {
            userLikeRepository.delete(existing.get());
            isNowLiked = false;
        } else {
            userLikeRepository.save(UserLike.of(user.getId(), bookId));
            isNowLiked = true;
        }

        return new BookResponse(book, isNowLiked);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new BookNotFoundException(id);
        bookRepository.deleteById(id);
    }

    // 현재 유저가 좋아요한 도서 ID Set 반환 (비로그인이면 빈 Set)
    private Set<Long> getUserLikedIds(String userEmail) {
        if (userEmail == null) return Set.of();
        return userRepository.findByEmail(userEmail)
                .map(user -> userLikeRepository.findByUserId(user.getId())
                        .stream().map(UserLike::getBookId).collect(Collectors.toSet()))
                .orElse(Set.of());
    }
}
