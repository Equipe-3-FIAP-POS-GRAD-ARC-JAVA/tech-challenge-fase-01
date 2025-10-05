package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class CreateOwnerUseCase implements UserCreateOwnerPort {

    private final UserRepositoryPort userRepository;

    public CreateOwnerUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createOwner(UserCreateRequest userCreateRequest) {
        var user = userCreateRequest.toDomain();
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRole(List.of(RolesEnum.OWNER));
        return userRepository.createClient(user);
    }
}
