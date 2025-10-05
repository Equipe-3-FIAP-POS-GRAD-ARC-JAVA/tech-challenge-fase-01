package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

public interface UserFindAuthenticatedUserByUsernamePort {

    UserResponse findAuthenticatedUserByUsername(String username);

}
