package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTOPorts(
        @NotBlank String login,
        @NotBlank String password
) {
}