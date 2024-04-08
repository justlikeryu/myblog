package com.example.myblog.controller;

import com.example.myblog.domain.Article;
import com.example.myblog.dto.ArticleListViewResponse;
import com.example.myblog.dto.ArticleViewResponse;
import com.example.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    private final BlogService blogService;

    //블로그 글 전체 목록을 담은 뷰 가져오기
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);

        return "articleList";//articleList.html 조회
    }

    //블로그 글 뷰 가져오기
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable(name = "id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }
}
