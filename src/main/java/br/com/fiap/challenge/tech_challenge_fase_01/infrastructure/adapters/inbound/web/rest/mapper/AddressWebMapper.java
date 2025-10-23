package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressUpdateRequestDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddressWebMapper {

    public AddressResponse toWebResponse(AddressResponse response) {
        if (response == null) {
            return null;
        }
        return new AddressResponse(
                response.id() != null ? response.id() : null,
                response.userId() != null ? response.userId() : null,
                response.street(),
                response.number(),
                response.city(),
                response.createdAt(),
                response.updatedAt()
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
                dto.city()
        );
    }

    public AddressUpdateRequest toApplicationUpdateRequest(AddressUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new AddressUpdateRequest(
                dto.street(),
                dto.number(),
                dto.city());
    }

}
