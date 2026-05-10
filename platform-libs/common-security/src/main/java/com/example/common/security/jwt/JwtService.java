package com.example.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final String secret;
    private final String issuer;
    private final Duration accessTokenLifetime;
    private SecretKey key;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.access-token-lifetime:PT1H}") Duration accessTokenLifetime
    ) {
        this.secret = secret;
        this.issuer = issuer;
        this.accessTokenLifetime = accessTokenLifetime;
    }

    @PostConstruct
    void init() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                "app.jwt.secret must be at least 32 characters; got " +
                (secret == null ? "null" : String.valueOf(secret.length())));
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String issueAccessToken(UUID userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenLifetime.toMillis());
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims verify(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
