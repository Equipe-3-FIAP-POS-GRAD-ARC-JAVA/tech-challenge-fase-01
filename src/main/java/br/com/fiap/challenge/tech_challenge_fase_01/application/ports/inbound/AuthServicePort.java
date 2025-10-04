package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;

public interface AuthServicePort {
    UserModel login(UserModel request);

    UserModel findAuthenticatedUserByUsername(String username);
}
