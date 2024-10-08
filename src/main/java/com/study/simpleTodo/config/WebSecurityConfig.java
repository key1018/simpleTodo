package com.study.simpleTodo.config;

import com.study.simpleTodo.security.JwtAuthenticationFilter;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeRequests(auth -> auth.requestMatchers("/","/auth/**").permitAll()
                        .anyRequest().authenticated()) // "/"와 "/auth/**"경로는 인증 안해도 됨. 그 이외의 경로는 인증해야됨
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session 기반이 아님을 선언
                .addFilterAfter(jwtAuthenticationFilter, CorsFilter.class) // 매 요청마다 CorsFilter 실행한 후 jwtAuthenticationFilter 실행
                .build();
    }
}
