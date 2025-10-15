package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de atualização de senha na camada web.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Validar dados de entrada HTTP
 * - Representar requisição de atualização de senha
 * 
 * Arquitetura Hexagonal:
 * - Pertence à camada de infraestrutura (adapter inbound web)
 * - Separado do DTO da camada de aplicação
 */
public record UpdatePasswordRequestDTO(

        @NotBlank(message = "A senha atual é obrigatória")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
        String newPassword,

        @NotBlank(message = "A confirmação da nova senha é obrigatória")
        String confirmPassword
) {}
