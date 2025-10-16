package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

public record UserUpdateRequest(
                String name,
                String email,
                String login) {
}