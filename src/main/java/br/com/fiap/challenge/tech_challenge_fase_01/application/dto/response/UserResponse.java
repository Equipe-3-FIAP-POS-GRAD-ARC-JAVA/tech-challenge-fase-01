package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email, String login) {
}