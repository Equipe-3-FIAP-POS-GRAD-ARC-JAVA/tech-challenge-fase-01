package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.JwtUtil;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthWebMapper {

    private final JwtUtil jwtUtil;

    public br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest 
            toApplicationLoginRequest(LoginRequest webDto) {
        String normalizedLogin = webDto.login().trim().toLowerCase();
        return new br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest(
            normalizedLogin,
            webDto.password());
    }

    public LoginResponse toWebLoginResponse(
            br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse appResponse) {

        List<String> roles = appResponse.user().roles() != null
            ? appResponse.user().roles().stream()
                .map(role -> "ROLE_" + role.name())
                .collect(java.util.stream.Collectors.toList())
            : List.of("ROLE_CLIENT");

        var userProfile = new UserProfileResponse(
                appResponse.user().id(),
                appResponse.user().email(),
                roles
        );

        return new LoginResponse(
                appResponse.token(),
                "Bearer",
                jwtUtil.getExpirationInSeconds(),
                userProfile
        );
    }
}