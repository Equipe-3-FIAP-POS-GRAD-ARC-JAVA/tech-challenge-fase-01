package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserUpdateRequestDTO;

public interface UserUseCases {
    
    UserResponseDTO createClient(UserCreateRequestDTO request);
    UserResponseDTO update(String id, UserUpdateRequestDTO request);
    UserResponseDTO updatePassword(String id, String password);
    List<UserResponseDTO> findByName(String name);
    void delete(String id);
    UserResponseDTO createOwner(UserCreateRequestDTO request);

}
