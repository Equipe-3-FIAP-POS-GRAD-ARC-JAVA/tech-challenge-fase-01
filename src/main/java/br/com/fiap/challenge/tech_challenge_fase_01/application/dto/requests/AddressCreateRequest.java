package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

import java.util.UUID;

public record AddressCreateRequest(
        UUID userId,
        String street,
        String number,
        String city) {
}