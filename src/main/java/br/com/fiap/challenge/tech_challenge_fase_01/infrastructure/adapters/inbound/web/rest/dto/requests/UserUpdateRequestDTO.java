package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
                @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres") String name,

                @Email(message = "Email inválido") String email,

                @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres") String login) {
}