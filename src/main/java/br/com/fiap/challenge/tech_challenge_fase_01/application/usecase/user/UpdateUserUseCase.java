package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.time.LocalDateTime;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UpdateUserUseCase implements UserUpdatePort {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        var user = userRepository.findByIdtoDomain(id);
        user.setName(userUpdateRequest.name());
        user.setEmail(userUpdateRequest.email());
        user.setLogin(userUpdateRequest.login());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.update(id, user);
    }
}
