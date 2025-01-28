package com.study.simpleTodo.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
    public static final String REDIRECT_URI_PARAM = "redirect_url";
    private static final int MAX_AGE = 180;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/auth/authorize")) {
            try{
                logger.info("redirecting to " + request.getRequestURI());
                // 리퀘스트 파라미터에서 redirect_url을 가져온다
                // 즉, redirect_url 매개변수의 값을 가져온 후 쿠키에 저장
                String redirectUrl = request.getParameter(REDIRECT_URI_PARAM);
                logger.info("Redirect Url to " + redirectUrl);
                Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(MAX_AGE);
                response.addCookie(cookie);
            } catch (Exception e){
                logger.error("Could not redirect to " + request.getRequestURI(), e);
                logger.info("Unauthorized request");
            }
        }
        filterChain.doFilter(request,response);
    }
}
