package me.bono039.springbootblog.dto;

import lombok.Getter;
import me.bono039.springbootblog.domain.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title   = article.getTitle();
        this.content = article.getContent();
    }
}
