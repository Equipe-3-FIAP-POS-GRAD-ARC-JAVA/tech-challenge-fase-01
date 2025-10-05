package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;

/**
 * Mapper para converter entre DTOs de autenticação da camada web e da camada de aplicação.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Converter DTOs de login entre camadas
 * - Isolar dependências entre camadas web e aplicação
 * 
 * Arquitetura Hexagonal:
 * - Faz parte do Adapter Inbound (web)
 * - Mantém independência entre as camadas
 */
@Component
public class AuthWebMapper {

    /**
     * Converte requisição de login da web para aplicação.
     */
    public br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest 
            toApplicationLoginRequest(LoginRequest webDto) {
        if (webDto == null) {
            return null;
        }
        return new br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest(
                webDto.login(),
                webDto.password());
    }

    /**
     * Converte resposta de login da aplicação para web.
     */
    public LoginResponse toWebLoginResponse(
            br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse appResponse) {
        if (appResponse == null) {
            return null;
        }
        return new LoginResponse(appResponse.token());
    }
}
