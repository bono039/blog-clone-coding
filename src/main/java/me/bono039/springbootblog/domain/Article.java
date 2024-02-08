package me.bono039.springbootblog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter                                             // 접근자 메소드 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 접근 제어자 : PROTECTED
public class Article {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder    // 빌더 패턴으로 객체 생성
    public Article(String title, String content) {
        this.title   = title;
        this.content = content;
    }

    @CreatedDate    // 엔티티 생성 시 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate   // 엔티티 수정 시 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
