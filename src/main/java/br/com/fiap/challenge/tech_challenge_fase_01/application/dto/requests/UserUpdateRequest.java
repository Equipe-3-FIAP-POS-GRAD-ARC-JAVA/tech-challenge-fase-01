package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

/**
 * DTO para requisição de atualização de usuário.
 * Usado na camada de aplicação (ports inbound).
 * 
 * Nota: A atualização é feita através do método updateInfo() do UserDomain.
 */
public record UserUpdateRequest(
                String name,
                String email,
                String login) {
}
