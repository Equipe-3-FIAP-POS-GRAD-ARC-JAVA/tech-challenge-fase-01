package br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenUseCases {
    String generateToken(UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
    String extractUsername(String token);
    LoginResponse getLoginResponse(String username);
}
