package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

public record UserCreateRequestDTOPorts(
        String name,
        String email,
        String login,
        String password) {

}
