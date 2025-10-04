package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;

public class UserUpdateUseCase implements UserUpdatePort {

    @Override
    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }
}
