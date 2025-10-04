package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;

public interface AuthServicePort {
    LoginResponse login(LoginRequest request);

    UserModel findAuthenticatedUserByUsername(String username);
}
