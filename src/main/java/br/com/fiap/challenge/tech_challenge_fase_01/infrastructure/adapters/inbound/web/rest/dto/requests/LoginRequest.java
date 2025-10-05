package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @NotBlank String login,
                @NotBlank String password) {
}