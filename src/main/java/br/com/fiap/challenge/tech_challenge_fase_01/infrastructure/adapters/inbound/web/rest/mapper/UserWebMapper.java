package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserUpdateRequestDTO;
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
                dto.password());
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
        return new UserResponseDTO(
                response.id().toString(),
                response.name(),
                response.email(),
                response.login());
    }
}