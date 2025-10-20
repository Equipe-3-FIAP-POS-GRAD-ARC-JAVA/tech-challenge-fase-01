package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address;

import java.util.UUID;

public interface AddressDeletePort {
    void deleteAddress(UUID userId, UUID addressId);
}
