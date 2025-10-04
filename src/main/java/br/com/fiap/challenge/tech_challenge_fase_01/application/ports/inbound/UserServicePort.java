package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;

public interface UserServicePort {

    public UserResponseDTOPorts create(UserCreateRequestDTOPorts userCreateRequestDTO);

    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO);

    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO);

    public List<UserResponseDTOPorts> getByName(String name);

    public void delete(String id);

    public UserResponseDTOPorts createOwner(UserCreateRequestDTOPorts userCreateRequestDTO);

    public UserResponseDTOPorts findAuthenticatedUserByUsername(String username);

}
