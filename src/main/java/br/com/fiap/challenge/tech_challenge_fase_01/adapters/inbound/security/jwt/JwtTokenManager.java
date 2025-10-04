package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public String generateToken(User user) {
        final String username = user.getLogin();

        // Evita problema de inferência de tipos
        final List<RolesEnum> userRoles = (user.getRoles() == null) ? List.of() : user.getRoles();

        // ["ROLE_ADMIN", "ROLE_DONO", ...]
        final String[] roleNames = userRoles.stream()
                .map(r -> "ROLE_" + r.name())
                .toArray(String[]::new);

        //@formatter:off
        return JWT.create()
                .withSubject(username)
                .withIssuer(jwtProperties.getIssuer())
                .withArrayClaim("roles", roleNames)
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
        return usernameFromToken.equals(authenticatedUsername) && !isTokenExpired(token);
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
