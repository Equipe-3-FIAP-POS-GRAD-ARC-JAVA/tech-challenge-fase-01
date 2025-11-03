package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

public record AddressUpdateRequest(
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String zipCode) {
}