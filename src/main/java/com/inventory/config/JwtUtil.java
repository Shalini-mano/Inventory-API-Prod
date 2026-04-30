package com.inventory.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.inventory.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // TODO: variables + constructor
    private final long expirationMs;
    private final SecretKey secretKey;

    public JwtUtil(
            @Value("${app.jwt.expirationMinutes}") long expirationMs,
            @Value("${app.jwt.secret}") String secret) {
        this.expirationMs = expirationMs * 1000L;
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Long userId, Role role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("role", role.name())
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact();
    }

    public boolean isTokenValid(String token) {
        // if verification or claim retrieval is erroring out,
        // then the token is invalid
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
    // Add this method to extract the subject (userId)
    public Long extractUserId(String token) {
        try {
            String subject = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            return Long.parseLong(subject);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: cannot extract userId", e);
        }
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}
