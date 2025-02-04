package com.study.simpleTodo.config;

import com.study.simpleTodo.security.JwtAuthenticationFilter;
import com.study.simpleTodo.security.OAuthSuccessHandler;
import com.study.simpleTodo.security.OAuthUserServiceImpl;
import com.study.simpleTodo.security.RedirectUrlCookieFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfigurationSource;  // 수정된 import
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService; // 우리가 만든 oAuthUserServiceImpl 추가

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    @Autowired
    private RedirectUrlCookieFilter redirectUrlCookieFilter;

    /**
     * GitHub로 OAuth 흐름을 시작하기 위한 기본 Spring Security URL인 http://localhost:8090/oauth2/authorization/github에 액세스하여 github 로그인 처리
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정 적용
                .authorizeRequests(auth -> auth.requestMatchers("/", "/login", "/auth/**","/oauth2/**").permitAll()
                        .anyRequest().authenticated())  // 인증되지 않은 요청은 /login으로 리다이렉트
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // stateless 설정
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint ->
                                endpoint.baseUri("/oauth2/callback/*")) // 리다이렉션 엔드포인트 설정
                        .authorizationEndpoint(endpoint ->
                                endpoint.baseUri("/auth/authorize")) // oauth2 시작을 위한 엔드포인트 추가
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuthUserService)) // userInfoEndpoint 설정
                        .successHandler(oAuthSuccessHandler) // OAuth2 성공 핸들러
                        .failureHandler((request, response, exception) -> { // OAuth2 실패 핸들러
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("OAuth 로그인 실패");
                        }))
                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // 인증 실패 시 401 응답
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))) // 인증을 못받을 떄 403을 받아 로그인 화면으로 리다이렉트
                .addFilterAfter(jwtAuthenticationFilter, CorsFilter.class)  // jwtAuthenticationFilter 실행
                .addFilterBefore(redirectUrlCookieFilter, OAuth2AuthorizationRequestRedirectFilter.class) // 리다이렉트 되기 전에 필터 실행
                .build();
    }

    private AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

            String redirectUrl = (savedRequest != null)
                    ? savedRequest.getRedirectUrl()  // 이전 요청 경로로 리다이렉트
                    : "/"; // 기본 경로

            response.sendRedirect(redirectUrl);
        };
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");  // 클라이언트 URL
        configuration.addAllowedMethod("*");  // 모든 메서드 허용
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.setAllowCredentials(true);  // 쿠키, 인증 정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}

