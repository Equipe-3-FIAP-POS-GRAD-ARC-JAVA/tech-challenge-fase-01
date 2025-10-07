package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt.JwtTokenManager;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.JwtTokenUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements JwtTokenUseCases {

    private final JwtTokenManager jwtTokenManager;
    private final UserDetailsService userDetailsService;

    @Override
    public String generateToken(UserDetails userDetails) {
        return jwtTokenManager.generateToken(userDetails);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenManager.validateToken(token, userDetails);
    }

    @Override
    public String extractUsername(String token) {
        return jwtTokenManager.extractUsername(token);
    }

    @Override
    public LoginResponse getLoginResponse(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenManager.generateToken(user);
        return new LoginResponse(token);
    }
}
