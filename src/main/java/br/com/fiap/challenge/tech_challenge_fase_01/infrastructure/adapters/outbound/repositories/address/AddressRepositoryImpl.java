package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddressNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.mappers.AddressEntityMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepositoryPort {

    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Address not found";

    private final JpaAddressRepository jpaAddressRepository;
    private final AddressEntityMapper addressMapper;

    @Override
    public AddressDomain save(AddressDomain address) {
        JpaAddressEntity entity = addressMapper.toEntity(address);
        JpaAddressEntity saved = jpaAddressRepository.save(entity);
        return addressMapper.toDomain(saved);
    }

    @Override
    public Optional<AddressDomain> findById(UUID id) {
        return jpaAddressRepository.findById(id).map(addressMapper::toDomain);
    }

    @Override
    public List<AddressDomain> findByAddressFromUser(UUID userId) {
        return executeWithExceptionHandling(
                () -> jpaAddressRepository.findAllByUser_IdOrderByCreatedAtDesc(userId)
                        .stream()
                        .map(addressMapper::toDomain)
                        .toList(),
                List.of(),
                "finding addresses by userId: " + userId
        );
    }

    @Override
    public void deleteAddress(UUID addressId) {
        jpaAddressRepository.deleteById(addressId);
    }


    private <T> T executeWithExceptionHandling(Supplier<T> operation, T defaultValue, String operationDescription) {
        try {
            return operation.get();
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private Supplier<AddressNotFoundException> createAddressNotFoundExceptionSupplier(UUID id) {
        return () -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MESSAGE + " with id: " + id);
    }
}
