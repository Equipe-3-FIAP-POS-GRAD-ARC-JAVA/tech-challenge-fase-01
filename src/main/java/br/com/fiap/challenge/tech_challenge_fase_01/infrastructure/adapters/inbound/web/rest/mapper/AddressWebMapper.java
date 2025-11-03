package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO;

@Component
public class AddressWebMapper {

    public AddressResponseDTO toWebResponse(AddressResponse response) {
        if (response == null) {
            return null;
        }
        return new AddressResponseDTO(
                response.id() != null ? response.id().toString() : null,
                response.userId() != null ? response.userId().toString() : null,
                response.street(),
                response.number(),
                response.complement(),
                response.neighborhood(),
                response.city(),
                response.zipCode()
        );
    }

    public AddressCreateRequest toApplicationRequest(AddressCreateRequestDTO dto, UUID userId) {
        if (dto == null) {
            return null;
        }
        return new AddressCreateRequest(
                userId,
                dto.street(),
                dto.number(),
                dto.complement(),
                dto.neighborhood(),
                dto.city(),
                dto.zipCode()
        );
    }

    public AddressUpdateRequest toApplicationUpdateRequest(AddressUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new AddressUpdateRequest(
                dto.street(),
                dto.number(),
                dto.complement(),
                dto.neighborhood(),
                dto.city(),
                dto.zipCode());
    }

    public AddressResponseDTO toResponseDTO(AddressResponse response) {
        if (response == null) {
            return null;
        }
        return new AddressResponseDTO(
                response.id() != null ? response.id().toString() : null,
                response.userId() != null ? response.userId().toString() : null,
                response.street(),
                response.number(),
                response.complement(),
                response.neighborhood(),
                response.city(),
                response.zipCode()
        );
    }

}
