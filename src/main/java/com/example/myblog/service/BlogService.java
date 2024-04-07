package com.example.myblog.service;

import com.example.myblog.domain.Article;
import com.example.myblog.dto.AddArticleRequest;
import com.example.myblog.dto.UpdateArticleRequest;
import com.example.myblog.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor//final이붙거나 NotNull인 필드의 생성자 추가
@Service//해당 클래스를 빈으로 서블릿 컨테이너에 등록
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 글 포스팅
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    //블로그 모든 글 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    //블로그 글 조회
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));
    }

    //블로그 글 삭제
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    //블로그 글 수정
    @Transactional//매칭한 메서드를 하나의 트랜잭션으로 묶는다 *트랜잭션이란? DB의 데이터를 바꾸기 위한 하나의 작업 단위
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
