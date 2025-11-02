package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.AddressMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class FindUserByIdUseCase implements UserFindByIdPort {

    private final UserRepositoryPort userRepository;
    private final AddressRepositoryPort addressRepository;

    public FindUserByIdUseCase(UserRepositoryPort userRepository, AddressRepositoryPort addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public UserResponse findById(UUID id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));

        var addresses = addressRepository.findByAddressFromUser(user.getId());
        var addressResponses = AddressMapper.toResponseList(addresses);

        return UserMapper.toResponse(user, addressResponses);
    }
}
