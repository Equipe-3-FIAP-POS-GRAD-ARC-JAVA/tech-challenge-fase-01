package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface UserFindByNamePort {

    public List<UserResponseDTOPorts> getByName(String name);

}
