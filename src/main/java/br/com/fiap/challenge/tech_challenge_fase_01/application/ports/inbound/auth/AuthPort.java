package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions.UnauthorizedException;

/**
 * Port Inbound para autenticação.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Definir contrato para autenticação de usuários
 * 
 * Arquitetura Hexagonal:
 * - Port do lado da aplicação (core)
 * - Implementado por Use Cases
 * - Usado por Adapters Inbound (controllers)
 */
public interface AuthPort {

    /**
     * Autentica um usuário e gera um token JWT.
     * 
     * @param request Credenciais do usuário
     * @return Token JWT se autenticação bem-sucedida
     * @throws UnauthorizedException se credenciais inválidas
     */
    LoginResponse login(LoginRequest request);
}
