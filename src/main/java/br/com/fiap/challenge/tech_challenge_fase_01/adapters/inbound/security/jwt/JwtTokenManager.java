package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public String generateToken(User user) {
        final String username = user.getLogin();
        final List<RolesEnum> userRoles = Objects.requireNonNullElse(user.getRole(), Collections.emptyList());

        // Converte para ["ROLE_ADMIN", "ROLE_OWNER", ...]
        final String[] roleNames = userRoles.stream()
                .filter(Objects::nonNull)
                .map(r -> "ROLE_" + r.name())
                .toArray(String[]::new);

        //@formatter:off
        return JWT.create()
                .withSubject(username)
                .withIssuer(jwtProperties.getIssuer())
                .withArrayClaim("roles", roleNames) // múltiplas roles
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMinute() * 60L * 1000L))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()));
        //@formatter:on
    }

    public String getUsernameFromToken(String token) {
        return getDecodedJWT(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getDecodedJWT(token).getClaim("roles").asList(String.class);
    }

    public boolean validateToken(String token, String authenticatedUsername) {
        final String usernameFromToken = getUsernameFromToken(token);
        final boolean equalsUsername = usernameFromToken.equals(authenticatedUsername);
        final boolean tokenExpired = isTokenExpired(token);
        return equalsUsername && !tokenExpired;
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDateFromToken = getExpirationDateFromToken(token);
        return expirationDateFromToken.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getDecodedJWT(token).getExpiresAt();
    }

    private DecodedJWT getDecodedJWT(String token) {
        final Algorithm alg = Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes());
        final JWTVerifier jwtVerifier = JWT.require(alg)
                .withIssuer(jwtProperties.getIssuer())
                .build();
        return jwtVerifier.verify(token);
    }
}
