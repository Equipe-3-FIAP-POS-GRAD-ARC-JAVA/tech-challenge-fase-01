package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

/**
 * DTO para requisição de login.
 * Usado na camada de aplicação (ports inbound).
 * 
 * Nota: Validações de formato são feitas na camada web (infrastructure).
 * Esta camada deve ser independente de frameworks.
 */
public record LoginRequest(
                String login,
                String password) {
}
