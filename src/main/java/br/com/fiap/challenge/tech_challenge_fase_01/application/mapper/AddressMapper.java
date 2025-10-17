package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class AddressMapper {

    private AddressMapper() {
        throw new UnsupportedOperationException("Utility class - não deve ser instanciada");
    }

    public static AddressResponse toResponse(AddressDomain addressDomain) {
        if (addressDomain == null) {
            return null;
        }

        return new AddressResponse(
                addressDomain.getId(),
                addressDomain.getUserId(),
                addressDomain.getStreet(),
                addressDomain.getNumber(),
                addressDomain.getCity(),
                addressDomain.getCreatedAt(),
                addressDomain.getUpdatedAt()
        );
    }

    public static List<AddressResponse> toResponseList(List<AddressDomain> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return List.of();
        }

        return addresses.stream()
                .map(AddressMapper::toResponse)
                .collect(Collectors.toList());
    }
}
