package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;

public interface UserUseCases {
    
    User create(UserRequestDTO request);
    User update(String id, UserRequestDTO request);
    User updatePassword(String id, String password);
    List<UserResponseDTO> findByName(String name);
    void delete(String id);

}
