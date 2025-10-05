package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.security;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.JwtTokenPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;

/**
 * Adapter Outbound que implementa o port de geração de tokens JWT.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Adaptar o JwtTokenManager para o port da aplicação
 * - Delegar operações de JWT para o JwtTokenManager
 * 
 * Arquitetura Hexagonal:
 * - Adapter Outbound (driven adapter)
 * - Implementa JwtTokenPort (port outbound)
 * - Usa JwtTokenManager (implementação específica de JWT)
 * - Mantém a camada de aplicação independente de detalhes de JWT
 */
@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenPort {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public String generateToken(UserDomain user) {
        return jwtTokenManager.generateToken(user);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenManager.getUsernameFromToken(token);
    }

    @Override
    public boolean validateToken(String token, String username) {
        return jwtTokenManager.validateToken(token, username);
    }
}
