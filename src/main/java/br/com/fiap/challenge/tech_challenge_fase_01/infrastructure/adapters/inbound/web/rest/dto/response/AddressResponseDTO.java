package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response;

public record AddressResponseDTO(
        String id,
        String userId,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String zipCode
) {}