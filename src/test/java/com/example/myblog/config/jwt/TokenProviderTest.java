package com.example.myblog.config.jwt;

import com.example.myblog.domain.User;
import com.example.myblog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저정보와 만료 기간을 전달하여 토큰 생성")
    @Test
    void generateToken() {
        //given 토큰에 유저 정보 추가를 하기 위해 테스트 유저 생성
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        //when 토큰 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        //then jjwt라이브러리를 통해 토큰의 복호화, 클레임에 넣어둔 id값이 given절에서 만든 유저아이디와 동일한지 확인
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretkey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰은 유효성 검증에 실패")
    @Test
    void validToken_invalidToken() {
        //given jjwt를 통한 토큰 생성(단, 이미 만료된 토큰)
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        //when 토큰의 유효성 확인
        boolean result = tokenProvider.validToken(token);

        //then 무효한 토큰임을 확인
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰은 유효성 검증에 성공")
    @Test
    void validToken_validToken() {
        //given jjwt라이브러리를 통한 (유효한) 토큰 생성
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        //when 토큰의 유효성 확인
        boolean result = tokenProvider.validToken(token);

        //then 유효한 토큰임을 확인
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보 호출")
    @Test
    void getAuthentication() {
        //given jjwt를 통한 토큰 생성, 토큰의 제목은 user@email.com
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        //when 인증 객체 반환
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then 반환받은 인증 객체의 유저명을 가져와 given에서 설정한 subject값과 같은지 확인 
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저ID 호출")
    @Test
    void getUserId() {
        //given jjwt라이브러리를 사용해 토큰을 생성, 이때 클레임은 키는 id, 값은 1이라는 유저아이디
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        //when 유저ID 반환
        Long userIdByToken = tokenProvider.getUserId(token);

        //then 반환받은 유저ID가 given에서 설정한 유저ID값과 같은지 확인
        assertThat(userIdByToken).isEqualTo(userId);
    }
}