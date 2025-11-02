package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;

@Component
public class AddressEntityMapper {

    public JpaAddressEntity toEntity(AddressDomain domain) {
        if (domain == null) return null;

        JpaAddressEntity e = new JpaAddressEntity();
        e.setId(domain.getId());
        e.setStreet(domain.getStreet());
        e.setNumber(domain.getNumber());
        e.setComplement(domain.getComplement());
        e.setNeighborhood(domain.getNeighborhood());
        e.setCity(domain.getCity());
        e.setZipCode(domain.getZipCode());
        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());

        if (domain.getUserId() != null) {
            JpaUserEntity userRef = new JpaUserEntity();
            userRef.setId(domain.getUserId());
            e.setUser(userRef);
        } else {
            e.setUser(null);
        }

        return e;
    }

    public AddressDomain toDomain(JpaAddressEntity entity) {
        if (entity == null) return null;

        UUID userId = null;
        if (entity.getUser() != null) {
            userId = entity.getUser().getId();
        }

        return AddressDomain.builder()
                .id(entity.getId())
                .userId(userId)
                .street(entity.getStreet())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .city(entity.getCity())
                .zipCode(entity.getZipCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
