package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de usuário na camada web.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Validar dados de entrada HTTP
 * - Representar requisição de criação de usuário
 * 
 * Arquitetura Hexagonal:
 * - Pertence à camada de infraestrutura (adapter inbound web)
 * - Separado do DTO da camada de aplicação
 */
public record UserCreateRequestDTO(
        @NotBlank(message = "Nome é obrigatório") @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres") String name,

        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,

        @NotBlank(message = "Login é obrigatório") @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres") String login,

        @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String password) {
}
