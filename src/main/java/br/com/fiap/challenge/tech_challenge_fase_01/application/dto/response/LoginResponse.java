package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

public record LoginResponse(
        String token,
        UserResponse user) {

}