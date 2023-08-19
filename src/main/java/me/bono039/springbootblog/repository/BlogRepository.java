package me.bono039.springbootblog.repository;

import me.bono039.springbootblog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
