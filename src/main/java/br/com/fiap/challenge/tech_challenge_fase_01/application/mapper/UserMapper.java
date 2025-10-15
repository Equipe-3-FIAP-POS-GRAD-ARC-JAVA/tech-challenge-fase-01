package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

/**
 * Mapper responsável por conversões entre UserDomain e DTOs.
 * 
 * Seguindo o princípio SRP (Single Responsibility Principle):
 * - DTOs são apenas estruturas de dados (sem lógica)
 * - Mapper tem a responsabilidade única de fazer conversões
 * 
 * Benefícios:
 * - DTOs mais simples e focados
 * - Conversões centralizadas e reutilizáveis
 * - Facilita testes das conversões
 * - Possibilita lógica de conversão complexa quando necessário
 */
public class UserMapper {

    /**
     * Converte UserDomain para UserResponse.
     * 
     * @param userDomain Objeto de domínio
     * @return DTO de resposta
     */
    public static UserResponse toResponse(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        }

        return new UserResponse(
                userDomain.getId(),
                userDomain.getName(),
                userDomain.getEmail(),
                userDomain.getLogin());
    }

    /**
     * Converte lista de UserDomain para lista de UserResponse.
     * 
     * @param users Lista de objetos de domínio
     * @return Lista de DTOs de resposta
     */
    public static List<UserResponse> toResponseList(List<UserDomain> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Construtor privado para evitar instanciação
    private UserMapper() {
        throw new UnsupportedOperationException("Utility class - não deve ser instanciada");
    }
}
