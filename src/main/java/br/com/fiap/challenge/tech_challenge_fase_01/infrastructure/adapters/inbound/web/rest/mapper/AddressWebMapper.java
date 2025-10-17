package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import org.springframework.stereotype.Component;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
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
                response.city(),
                response.createdAt(),
                response.updatedAt()
        );
    }
}
