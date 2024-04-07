package com.example.myblog.dto;

import com.example.myblog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity(){//toEntity는 빌더 패턴을 사용해 DTO를 엔터티로 만들어준다
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
