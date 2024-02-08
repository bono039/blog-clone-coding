package me.bono039.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import me.bono039.springbootblog.dto.ArticleListViewResponse;
import me.bono039.springbootblog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}