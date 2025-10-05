package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

/**
 * Port Outbound para geração e validação de tokens JWT.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Definir contrato para operações com JWT
 * 
 * Arquitetura Hexagonal:
 * - Port do lado da aplicação (core)
 * - Implementado por Adapters Outbound (infraestrutura)
 * - Permite que a aplicação use JWT sem conhecer detalhes de implementação
 * - Facilita testes (pode ser mockado)
 */
public interface JwtTokenPort {

    /**
     * Gera um token JWT para o usuário.
     * 
     * @param user Domínio do usuário
     * @return Token JWT assinado
     */
    String generateToken(UserDomain user);

    /**
     * Extrai o username do token JWT.
     * 
     * @param token Token JWT
     * @return Username do usuário
     */
    String getUsernameFromToken(String token);

    /**
     * Valida um token JWT.
     * 
     * @param token Token JWT
     * @param username Username para validar
     * @return true se válido, false caso contrário
     */
    boolean validateToken(String token, String username);
}
