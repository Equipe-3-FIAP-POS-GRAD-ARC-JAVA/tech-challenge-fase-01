package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final SecretKey key;
    private static final long EXPERATION_MS = 86400000;

    public JwtUtil() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.length() < 32) {
            secret = "my-secret-key-for-jwt-token-generation-that-is-32-chars-or-more";
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, List<String> roles) {
        JwtBuilder builder = Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPERATION_MS))
                .signWith(key);
        return builder.compact();
    }

    public long getExpirationInSeconds() {
        return EXPERATION_MS / 1000;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}