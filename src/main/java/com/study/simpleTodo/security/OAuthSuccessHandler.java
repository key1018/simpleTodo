package com.study.simpleTodo.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.study.simpleTodo.security.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider; // 생성자로 주입받은 TokenProvider

    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)throws IOException {
        logger.info("auth....");
//        TokenProvider tokenProvider = new TokenProvider();
        logger.info("auth success");
        // 주입받은 tokenProvider 사용
        String token = tokenProvider.create(authentication);

        // 쿠키에서 redirect_url을 가져와 리다이렉트
        Optional<Cookie> redirectCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> REDIRECT_URI_PARAM.equals(cookie.getName()))
                .findFirst();

        String redirectUrl = redirectCookie
                .map(Cookie::getValue)
                .orElse(LOCAL_REDIRECT_URL);

////        response.getWriter().write(token);
        logger.info("Token : " + token);
        // 프론트엔드 리다이렉트 (프론트엔드에 토큰 전달)
//        response.sendRedirect("http://localhost:3000/sociallogin?token=" + token);
//        response.sendRedirect(redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL) + "/sociallogin?token=" + token);
        response.sendRedirect(redirectUrl + "/SocialLogin?token=" + token);
    }
}
