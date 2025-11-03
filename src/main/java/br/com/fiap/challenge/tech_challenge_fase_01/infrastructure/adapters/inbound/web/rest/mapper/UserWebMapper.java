package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserResponseDTO;

@Component
public class UserWebMapper {

    public UserCreateRequest toApplicationRequest(UserCreateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserCreateRequest(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                dto.street(),
                dto.number(),
                dto.complement(),
                dto.neighborhood(),
                dto.city(),
                dto.zipCode()
        );
    }

    public UserUpdateRequest toApplicationUpdateRequest(UserUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserUpdateRequest(
                dto.name(),
                dto.email(),
                dto.login());
    }

    public UpdatePasswordRequest toApplicationPasswordRequest(UpdatePasswordRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UpdatePasswordRequest(
                dto.currentPassword(),
                dto.newPassword(),
                dto.confirmPassword());
    }

    public UserResponseDTO toWebResponse(UserResponse response) {
        if (response == null) {
            return null;
        }
        
        List<AddressResponseDTO> addressesDTO = response.addresses() != null 
            ? response.addresses().stream()
                .map(this::toAddressWebResponse)
                .collect(Collectors.toList())
            : List.of();

        List<String> rolesDTO = response.roles() != null
            ? response.roles().stream()
                .map(Enum::name)
                .collect(Collectors.toList())
            : List.of();
        
        return new UserResponseDTO(
                response.id().toString(),
                response.name(),
                response.email(),
                response.login(),
                rolesDTO,
                addressesDTO);
    }

    private AddressResponseDTO toAddressWebResponse(AddressResponse address) {
        if (address == null) {
            return null;
        }
        return new AddressResponseDTO(
                address.id() != null ? address.id().toString() : null,
                address.userId() != null ? address.userId().toString() : null,
                address.street(),
                address.number(),
                address.complement(),
                address.neighborhood(),
                address.city(),
                address.zipCode()
        );
    }
}