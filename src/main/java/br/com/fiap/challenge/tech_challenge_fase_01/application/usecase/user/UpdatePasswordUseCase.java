package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.time.LocalDateTime;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UpdatePasswordUseCase implements UserUpdatePasswordPort {

    private final UserRepositoryPort userRepository;

    public UpdatePasswordUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO) {
        var user = userRepository.findByIdtoDomain(id);
        user.setPassword(updatePasswordRequestDTO.password());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.update(id, user);
    }
}
