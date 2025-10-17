package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;

import java.util.List;

public interface AddressCreatePort {
    AddressResponse createAddress(AddressCreateRequest addressCreateRequest);
}
