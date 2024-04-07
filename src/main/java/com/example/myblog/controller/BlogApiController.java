package com.example.myblog.controller;

import com.example.myblog.domain.Article;
import com.example.myblog.dto.AddArticleRequest;
import com.example.myblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController//Http 응답으로 객체 데이터를 JSON형식으로 반환함
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request){
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)//CREATED: 요청이 성공적으로 수행되었고 새로운 리소스가 생성됨
                .body(savedArticle);
    }
}
