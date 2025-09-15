package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response;

public record TokenResponse(
        String token,
        long expiresAtEpochSeconds
) {
}