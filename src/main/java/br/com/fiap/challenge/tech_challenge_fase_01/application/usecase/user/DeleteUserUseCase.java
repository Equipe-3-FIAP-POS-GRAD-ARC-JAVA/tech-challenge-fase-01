package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class DeleteUserUseCase implements UserDeletePort {

    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void delete(String id) {
        userRepository.delete(id);
    }
}
