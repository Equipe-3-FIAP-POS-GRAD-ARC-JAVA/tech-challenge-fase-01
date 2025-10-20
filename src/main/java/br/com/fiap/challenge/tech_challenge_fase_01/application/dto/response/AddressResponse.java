package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressResponse(
        UUID id,
        UUID userId,
        String street,
        String number,
        String city,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
