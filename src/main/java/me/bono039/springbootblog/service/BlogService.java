package me.bono039.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.dto.AddArticleRequest;
import me.bono039.springbootblog.dto.UpdateArticleRequest;
import me.bono039.springbootblog.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor    // final이나 @NotNull이 붙은 필드의 생성자 추가
@Service                    // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // [블로그 글 추가 메소드]
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    // [블로그 글 목록 조회 메소드] DB에 저장된 글 모두 가져옴.
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // [블로그 글 단건 조회]
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // [블로그 글 삭제]
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);    // 게시글 작성한 유저인지 확인
        blogRepository.deleteById(id);
    }

    // [블로그 글 수정]
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);    // 게시글 작성한 유저인지 확인
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 게시글 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
