package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.factory;



import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;

/**
 * Factory para criação de DTOs de resposta seguindo princípios SOLID.
 * 
 * Responsabilidades (SRP):
 * - Criar DTOs de resposta da camada web
 * - Centralizar lógica de criação de DTOs
 * - Aplicar padrão Factory Method
 * 
 * Arquitetura Hexagonal:
 * - Componente da camada de infraestrutura
 * - Facilita conversão entre camadas
 * - Mantém lógica de criação isolada
 * 
 * Princípios SOLID aplicados:
 * - SRP: Responsabilidade única de criação de DTOs
 * - OCP: Extensível para novos tipos de DTO
 * - DIP: Interface pode ser abstraída se necessário
 */
@Component
@RequiredArgsConstructor
public class ResponseDTOFactory {

    private final UserWebMapper userWebMapper;

    /**
     * Cria UserResponseDTO a partir de UserResponse.
     * Aplica Factory Method pattern delegando para o mapper existente.
     * 
     * @param userResponse resposta da camada de aplicação
     * @return DTO da camada web
     */
    public UserResponseDTO createUserResponseDTO(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }

        return userWebMapper.toWebResponse(userResponse);
    }

    /**
     * Factory method para criar DTO de resposta de erro.
     * Centraliza criação de DTOs de erro padronizados.
     * 
     * @param message mensagem de erro
     * @param details detalhes adicionais
     * @return DTO de erro padronizado
     */
    public ErrorResponseDTO createErrorResponseDTO(String message, String details) {
        return ErrorResponseDTO.builder()
                .message(message)
                .details(details)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    /**
     * DTO interno para respostas de erro.
     * Encapsula estrutura de erro padronizada.
     */
    @lombok.Builder
    @lombok.Data
    public static class ErrorResponseDTO {
        private String message;
        private String details;
        private java.time.LocalDateTime timestamp;
    }
}