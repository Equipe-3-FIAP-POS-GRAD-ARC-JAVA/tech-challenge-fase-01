package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UpdateUserUseCase implements UserUpdatePort {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse update(UUID id, UserUpdateRequest userUpdateRequest) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));

        user.updateInfo(
                userUpdateRequest.name(),
                userUpdateRequest.email(),
                userUpdateRequest.login());

        var updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}