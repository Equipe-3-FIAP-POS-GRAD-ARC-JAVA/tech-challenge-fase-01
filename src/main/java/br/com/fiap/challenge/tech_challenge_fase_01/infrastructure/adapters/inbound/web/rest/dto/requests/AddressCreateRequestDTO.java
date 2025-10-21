package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressCreateRequestDTO(

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 3, max = 100, message = "Rua deve ter entre 3 e 100 caracteres")
        String street,

        @Size(max = 10, message = "Número deve ter entre 1 e 10 caracteres")
        String number,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 2, max = 50, message = "Cidade deve ter entre 2 e 50 caracteres")
        String city
) {
}