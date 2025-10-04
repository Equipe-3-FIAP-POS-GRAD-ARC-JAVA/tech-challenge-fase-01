package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.time.LocalDateTime;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UpdateUserUseCase implements UserUpdatePort {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO) {
        var user = userRepository.findByIdtoDomain(id);
        user.setName(userUpdateRequestDTO.name());
        user.setEmail(userUpdateRequestDTO.email());
        user.setLogin(userUpdateRequestDTO.login());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.update(id, user);
    }
}
