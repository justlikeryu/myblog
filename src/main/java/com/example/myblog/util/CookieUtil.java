package com.example.myblog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
//OAuth2 인증 플로우를 구현하면 쿠키를 사용할 일이 발생한다.
//그때마다 쿠키를 생성하고 삭제하는 로직을 추가하면 불편하므로 유틸리티로 사용할 쿠키 관리 클래스
public class CookieUtil {
    //요청값(이름, 값, 만료기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    //쿠키명을 입력받으면 쿠키 삭제
    //실제로 쿠키를 삭제하는 방법은 없으므로 파라미터로 넘어온 키의 쿠키를 빈 값으로 바꾸고 만료시간을 0으로 설정
    //쿠키는 재생성되자마자 만료 처리가 된다
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);

                response.addCookie(cookie);
            }
        }
    }

    //객체를 직렬화하여 쿠키값으로 변환
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    //쿠키를 역직렬화하여 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> tClass) {
        return tClass.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
