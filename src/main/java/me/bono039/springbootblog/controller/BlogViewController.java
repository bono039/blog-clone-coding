package me.bono039.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.dto.ArticleListViewResponse;
import me.bono039.springbootblog.dto.ArticleViewResponse;
import me.bono039.springbootblog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    // 블로그 글 목록 가져오기
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);  // 1. 블로그 글 리스트 저장

        return "articleList";   // 2. articleList.html이라는 뷰 조회
    }

    // 블로그 글 가져오기
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    // 수정 화면 보여주기
    @GetMapping("/new-article")
    // 1. id 키를 가진 쿼리 파라미터 값을 id 변수에 매핑 (id는 없을 수도 있음)
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if(id == null) {    // 2. id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        }
        else {  // 3. id가 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }

}
