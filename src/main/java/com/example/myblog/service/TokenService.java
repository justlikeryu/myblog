package com.example.myblog.service;

import com.example.myblog.config.jwt.TokenProvider;
import com.example.myblog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    //1. 리프레시 토큰으로 유효성 검증하고
    //2. 유효하면 리프레시 토큰으로 사용자 아이디를 찾아서
    //3. 사용자 아이디로 사용자를 찾아
    //4. 토큰 제공자의 generateToken으로 새로운 액세스 토큰 생성!
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
