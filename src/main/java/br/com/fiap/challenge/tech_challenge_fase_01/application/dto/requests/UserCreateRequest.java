package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

public record UserCreateRequest(
        String name,
        String email,
        String login,
        String password,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String zipCode) {
}