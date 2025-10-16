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

    public br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest 
            toApplicationLoginRequest(LoginRequest webDto) {
        // Normaliza login para lowercase
        String normalizedLogin = webDto.login().trim().toLowerCase();
        return new br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest(
            normalizedLogin,
            webDto.password());
    }

    public LoginResponse toWebLoginResponse(
            br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse appResponse) {
        return new LoginResponse(appResponse.token());
    }
}
