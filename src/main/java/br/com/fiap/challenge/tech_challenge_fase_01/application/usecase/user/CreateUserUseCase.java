package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class CreateUserUseCase implements UserCreatePort {

    private final UserRepositoryPort userRepository;

    public CreateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        var user = userCreateRequest.toDomain();
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRole(List.of(RolesEnum.CLIENT));
        return userRepository.createClient(user);
    }
}
