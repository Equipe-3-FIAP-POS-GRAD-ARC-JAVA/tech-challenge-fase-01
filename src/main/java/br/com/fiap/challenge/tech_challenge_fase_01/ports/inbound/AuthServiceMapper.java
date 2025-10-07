package br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

public interface AuthServiceMapper {

    LoginResponse login(LoginRequest request);

    User findAuthenticatedUserByUsername(String username);
}
