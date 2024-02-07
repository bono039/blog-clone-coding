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
<<<<<<< HEAD

    // [블로그 글 단건 조회]
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // [블로그 글 삭제]
    public void delete(long id) {
        blogRepository.deleteById(id);
    }
=======
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
}
