package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;

public interface UserFindByNamePort {

    List<UserResponse> findByName(String name);

}
