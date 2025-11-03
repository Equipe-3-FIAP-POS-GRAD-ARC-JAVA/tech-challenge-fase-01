package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service;

import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddresDoesNotBelongToUserException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddressNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

public class AddressDomainService {

    private final AddressRepositoryPort addressRepository;

    public AddressDomainService(AddressRepositoryPort addressRepositoryPort) {
        this.addressRepository = addressRepositoryPort;
    }

    public void ensureAddressBelongsToUser(UUID userId, UUID addressId) {
        var address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new AddressNotFoundException("Endereço não encontrado: " + addressId));

        if (!address.getUserId().equals(userId)) {
            throw new AddresDoesNotBelongToUserException(
                    "O endereço " + addressId + " não pertence ao usuário " + userId + ".");
        }
    }
}
