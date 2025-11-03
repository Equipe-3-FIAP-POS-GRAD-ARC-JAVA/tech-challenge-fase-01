package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.AddressMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

public class CreateAddressUseCase implements AddressCreatePort {

    private final AddressRepositoryPort addressRepository;

    public CreateAddressUseCase(
            AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressResponse createAddress(AddressCreateRequest addressCreateRequest) {

        var address = AddressDomain.create(
                addressCreateRequest.userId(),
                addressCreateRequest.street(),
                addressCreateRequest.number(),
                addressCreateRequest.complement(),
                addressCreateRequest.neighborhood(),
                addressCreateRequest.city(),
                addressCreateRequest.zipCode()
        );

        var savedAdress = addressRepository.save(address);

        return AddressMapper.toResponse(savedAdress);
    }
}