package me.bono039.springbootblog.service;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.dto.AddArticleRequest;
import me.bono039.springbootblog.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor    // final이나 @NotNull이 붙은 필드의 생성자 추가
@Service                    // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // [블로그 글 추가 메소드]
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // [블로그 글 목록 조회 메소드] DB에 저장된 글 모두 가져옴.
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
}
