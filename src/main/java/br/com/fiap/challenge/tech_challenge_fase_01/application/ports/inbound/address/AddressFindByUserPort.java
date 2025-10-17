package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressFindByUserPort {
    List<AddressResponse> findAddress(UUID id);
}
