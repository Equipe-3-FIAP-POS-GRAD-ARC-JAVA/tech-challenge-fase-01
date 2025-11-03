package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.AddressMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class FindUserByNameUseCase implements UserFindByNamePort {

    private final UserRepositoryPort userRepository;
    private final AddressRepositoryPort addressRepository;

    public FindUserByNameUseCase(UserRepositoryPort userRepository, AddressRepositoryPort addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<UserResponse> findByName(String name) {

        var users = userRepository.findByName(name);

        // Para cada usuário, buscar seus endereços
        return users.stream()
                .map(user -> {
                    var addresses = addressRepository.findByAddressFromUser(user.getId());
                    var addressResponses = AddressMapper.toResponseList(addresses);
                    return UserMapper.toResponse(user, addressResponses);
                })
                .collect(Collectors.toList());
    }
}
