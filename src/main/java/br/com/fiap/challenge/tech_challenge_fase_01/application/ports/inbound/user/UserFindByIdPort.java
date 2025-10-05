package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

public interface UserFindByIdPort {

    UserResponse findById(UUID id);

}
