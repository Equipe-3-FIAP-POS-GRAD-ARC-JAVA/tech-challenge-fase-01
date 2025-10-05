package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login.
 * Usado na camada de aplicação (ports inbound).
 */
public record LoginRequest(
        @NotBlank String login,
        @NotBlank String password) {
}
