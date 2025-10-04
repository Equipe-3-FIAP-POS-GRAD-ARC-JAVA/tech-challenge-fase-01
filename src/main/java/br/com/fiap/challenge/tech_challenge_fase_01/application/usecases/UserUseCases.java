package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.UserServicePort;

public class UserUseCases implements UserServicePort {

    @Override
    public UserResponseDTOPorts create(UserCreateRequestDTOPorts userCreateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResponseDTOPorts> getByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public UserResponseDTOPorts createOwner(UserCreateRequestDTOPorts userCreateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTOPorts findAuthenticatedUserByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

}
