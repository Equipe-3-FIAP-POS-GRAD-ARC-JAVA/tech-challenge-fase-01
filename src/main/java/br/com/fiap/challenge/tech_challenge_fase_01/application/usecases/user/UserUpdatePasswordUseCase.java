package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;

public class UserUpdatePasswordUseCase implements UserUpdatePasswordPort {

    @Override
    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }
}
