package br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.dto.AuthenticatedUserDto;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;

import java.util.List;

public interface JwtTokenUseCases {

    public LoginResponse getLoginResponse(String login);

}
