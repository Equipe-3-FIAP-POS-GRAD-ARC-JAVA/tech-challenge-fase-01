package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class FindUserByNameUseCase implements UserFindByNamePort {

    private final UserRepositoryPort userRepository;

    public FindUserByNameUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTOPorts> findByName(String name) {
        return this.userRepository.findByName(name);
    }
}
