package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.AddressDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

import java.util.UUID;

public class DeleteAddressUseCase implements AddressDeletePort {

    private final AddressRepositoryPort addressRepository;
    private final AddressDomainService addressDomainService;

    public DeleteAddressUseCase(AddressRepositoryPort addressRepository,
                                AddressDomainService addressDomainService) {
        this.addressRepository = addressRepository;
        this.addressDomainService = addressDomainService;
    }

    @Override
    public void deleteAddress(UUID userId, UUID addressId) {
        addressDomainService.ensureAddressBelongsToUser(userId, addressId);

        addressRepository.deleteAddress(addressId);
    }
}
