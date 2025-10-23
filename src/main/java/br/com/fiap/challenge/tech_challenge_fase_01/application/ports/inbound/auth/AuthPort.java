package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse;

public interface AuthPort {

    LoginResponse login(LoginRequest request);
}