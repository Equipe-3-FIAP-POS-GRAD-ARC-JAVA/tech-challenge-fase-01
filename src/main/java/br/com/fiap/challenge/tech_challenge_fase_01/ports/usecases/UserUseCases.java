package br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.dto.AuthenticatedUserDto;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;

public interface UserUseCases {
    
    UserResponseDTO create(UserRequestDTO request);
    UserResponseDTO update(String id, UserRequestDTO request);
    UserResponseDTO updatePassword(String id, String password);
    List<UserResponseDTO> findByName(String name);
    void delete(String id);
    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

}
