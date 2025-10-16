package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;

@Component
public class AuthWebMapper {

    public br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest 
            toApplicationLoginRequest(LoginRequest webDto) {
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