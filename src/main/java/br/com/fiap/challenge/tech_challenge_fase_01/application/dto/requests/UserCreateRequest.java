package br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests;

/**
 * DTO para requisição de criação de usuário.
 * Usado na camada de aplicação (ports inbound).
 * 
 * Nota: A conversão para Domain não é mais responsabilidade do DTO.
 * Use os factory methods do UserDomain (createClient, createOwner).
 */
public record UserCreateRequest(
                String name,
                String email,
                String login,
                String password) {
}
