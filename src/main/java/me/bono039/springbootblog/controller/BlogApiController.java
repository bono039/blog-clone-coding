package me.bono039.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.dto.AddArticleRequest;
import me.bono039.springbootblog.dto.ArticleResponse;
import me.bono039.springbootblog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;

    // HTTP 메소드가 POST일 때 전달받은 URL과 같으면 메소드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        // 요청한 자원이 성공적으로 생성되었으며, 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(savedArticle);
    }

    // 글 전체 조회 메소드
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)  // 응답용 객체인 ArticleResponse로 파싱해 body에 담아 클라이언트에게 전송
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }
}
