package com.study.simpleTodo.security;


import com.study.simpleTodo.model.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.security.sasl.AuthenticationException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTH_KEY = "userId";
    private static final String ISSUER = "demo";
//    private static final String SUBJECT = "Auth";
    private static final int EXPIRATION_MILLIS = 1000 * 60 * 60 * 24; // 24h
    @Value("${jwt.secret.key}")
    private String secret;
    private SecretKey secretKey;

    @PostConstruct
    public void initSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(final UserEntity user) {
        Instant now = Instant.now();
        Date nowDate = Date.from(now);
        Date expiration = Date.from(
                now.plus(EXPIRATION_MILLIS, ChronoUnit.MINUTES));

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS512) // 생성한 키로 서명
                .claim(AUTH_KEY, user.getId()) // claim 방식(Json Object)으로 페이로드 구성: userId key-value 쌍을 페이로드로 주입
                .issuer(ISSUER) // 토큰 발급자
                .issuedAt(nowDate) // 토큰 발급 일시
//                .subject(SUBJECT)
                .subject(user.getId()) // 토큰 제목 : 토큰에서 사용자에 대한 식별값
                .notBefore(nowDate) // 토큰이 해당 시간 이후 활성화됨을 보장
                .expiration(expiration) // 토큰 만료 일시
                .compact(); // 토큰 압축
    }

    public String verifyTokenAndGetUserId(final String token) {
        try {

            Jws<Claims> jwt = Jwts.parser()
                    .verifyWith(secretKey) // JWS 는 토큰 생성할 때와 동일한 키로 파싱 필요
                    .build() // 파서를 빌드
                    .parseSignedClaims(token); // 서명된 클레임(Token)를 파싱

            Claims payload = jwt.getPayload();
            return payload.get(AUTH_KEY, String.class);

        } catch (SignatureException e) {
            throw new IllegalArgumentException("Key is invalid: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException("Token is expired: " + e.getMessage());
        }
    }
}