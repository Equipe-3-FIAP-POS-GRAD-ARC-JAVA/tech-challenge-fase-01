package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response;

import java.util.UUID;

/**
 * DTO para resposta de usuário.
 * 
 * Record puro de dados - sem lógica de conversão (SRP).
 * As conversões são feitas por UserMapper.
 * 
 * Usado na camada de aplicação (ports inbound).
 */
public record UserResponse(UUID id, String name, String email, String login) {
}
