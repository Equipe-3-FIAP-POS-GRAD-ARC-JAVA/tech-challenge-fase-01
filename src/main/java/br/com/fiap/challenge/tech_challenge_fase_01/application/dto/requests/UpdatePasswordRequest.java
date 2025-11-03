package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

public record UpdatePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {}