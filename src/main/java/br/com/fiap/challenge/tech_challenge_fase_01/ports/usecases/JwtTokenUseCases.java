package br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;

public interface JwtTokenUseCases {

    public LoginResponse getLoginResponse(String login);

}
