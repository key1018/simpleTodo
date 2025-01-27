package com.study.simpleTodo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider; // 생성자로 주입받은 TokenProvider

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)throws IOException {
        System.out.println("권한 :" + authentication);
//        TokenProvider tokenProvider = new TokenProvider();
        // 주입받은 tokenProvider 사용
        String token = tokenProvider.create(authentication);
        response.getWriter().write(token);
        log.info("token {}", token);
    }
}
