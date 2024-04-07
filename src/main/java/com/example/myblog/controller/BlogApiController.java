package com.example.myblog.controller;

import com.example.myblog.domain.Article;
import com.example.myblog.dto.AddArticleRequest;
import com.example.myblog.dto.ArticleResponse;
import com.example.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController//Http 응답으로 객체 데이터를 JSON형식으로 반환함
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)//CREATED: 요청이 성공적으로 수행되었고 새로운 리소스가 생성됨
                .body(savedArticle);
    }

    //findAll()메서드로 전체 글을 조회하여 응답용 객체인 ArticleResponse로 파싱해 body에 담아 전송
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/article/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }
}
