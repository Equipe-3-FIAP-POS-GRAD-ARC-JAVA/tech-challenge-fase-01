package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenManager {

    @Value("${security.jwt.secret}")
    private String secretKeyRaw;

    @Value("${security.jwt.expiration-ms:86400000}") // 24h
    private long jwtExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    void initKey() {
        // Para HS256 o segredo precisa ter >= 256 bits (32 bytes).
        // Use Base64 ou uma string longa. Aqui usamos UTF-8 direto:
        this.signingKey = Keys.hmacShaKeyFor(secretKeyRaw.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSigningKey() {
        return this.signingKey;
    }

    // Gera token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(exp)
                .signWith(getSigningKey(), Jwts.SIG.HS256) // API 0.12.x
                .compact();
    }

    // Extrai o login/email (subject)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Valida o token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // API 0.12.x
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
