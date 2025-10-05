package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class FindUserByIdUseCase implements UserFindByIdPort {

    private final UserRepositoryPort userRepository;

    public FindUserByIdUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse findById(String id) {
        return this.userRepository.findById(id);
    }
}
