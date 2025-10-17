package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.AddressMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUser;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

import java.util.List;
import java.util.UUID;

public class FindAddressUseCase implements AddressFindByUser {

    private final AddressRepositoryPort addressRepository;

    public FindAddressUseCase(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressResponse> findAddress(UUID id) {
        return addressRepository.findByUser(id)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
    }
}
