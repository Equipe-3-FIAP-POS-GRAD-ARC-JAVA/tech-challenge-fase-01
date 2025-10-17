package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepositoryPort {

    AddressDomain save(AddressDomain address);

    Optional<AddressDomain> findById(UUID id);

    List<AddressDomain> findByAddressFromUser(UUID userId);

    void deleteAddress(UUID addressId);
}
