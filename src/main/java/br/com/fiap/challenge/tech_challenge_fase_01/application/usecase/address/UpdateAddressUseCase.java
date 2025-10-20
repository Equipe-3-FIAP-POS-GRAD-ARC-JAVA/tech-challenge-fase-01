package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.AddressDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddressNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.AddressMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

import java.util.UUID;

public class UpdateAddressUseCase implements AddressUpdatePort {

    private final AddressRepositoryPort addressRepository;
    private final AddressDomainService addressDomainService;

    public UpdateAddressUseCase(AddressRepositoryPort addressRepository, AddressDomainService addressDomainService) {
        this.addressRepository = addressRepository;
        this.addressDomainService = addressDomainService;
    }

    @Override
    public AddressResponse updateAddress(UUID userId,
                                         UUID addressId,
                                         AddressUpdateRequest addressUpdateRequest) {

        addressDomainService.ensureAddressBelongsToUser(userId, addressId);

        var address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Endereço não encontrado com ID: " + addressId));;


        address.updateInfo(
                addressUpdateRequest.street(),
                addressUpdateRequest.number(),
                addressUpdateRequest.city());

        var updatedUser = addressRepository.save(address);

        return AddressMapper.toResponse(updatedUser);
    }
}