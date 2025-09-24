package br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;

import java.util.List;

public interface UserServiceMapper {

    public UserResponseDTO create(UserCreateRequestDTO userCreateRequestDTO);

    public UserResponseDTO update(String id, UserUpdateRequestDTO userUpdateRequestDTO);

    public UserResponseDTO updatePassword(String id, UpdatePasswordRequestDTO updatePasswordRequestDTO);

    public List<UserResponseDTO> getByName(String name);

    public void delete(String id);

    public UserResponseDTO createOwner(UserCreateRequestDTO userCreateRequestDTO);

//    public User findAuthenticatedUserByUsername(String username);

}
