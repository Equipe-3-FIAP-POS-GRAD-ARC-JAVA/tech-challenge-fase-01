package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;

public class UserFindByNameUseCase implements UserFindByNamePort {

    @Override
    public List<UserResponseDTOPorts> findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }
}
