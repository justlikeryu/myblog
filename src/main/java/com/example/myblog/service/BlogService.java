package com.example.myblog.service;

import com.example.myblog.domain.Article;
import com.example.myblog.dto.AddArticleRequest;
import com.example.myblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor//final이붙거나 NotNull인 필드의 생성자 추가
@Service//해당 클래스를 빈으로 서블릿 컨테이너에 등록
public class BlogService {
    private final BlogRepository blogRepository;

    //블로그 글 포스팅
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    //블로그 모든 글 조회
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
}
