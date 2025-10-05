package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

/**
 * DTO para requisição de atualização de senha.
 * Usado na camada de aplicação (ports inbound).
 */
public record UpdatePasswordRequest(
        String password) {
}
