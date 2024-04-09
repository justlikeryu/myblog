package com.example.myblog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//yml에서 설정한 이슈 발급자와 비밀키 값들을 변수로 접근하는데 사용하는 클래스
@Setter
@Getter
@Component
@ConfigurationProperties("jwt")//자바 클래스에 프로퍼티 값을 가져와서 사용하는 어노테이션
public class JwtProperties {
    private String issuer;
    private String secretkey;
}
