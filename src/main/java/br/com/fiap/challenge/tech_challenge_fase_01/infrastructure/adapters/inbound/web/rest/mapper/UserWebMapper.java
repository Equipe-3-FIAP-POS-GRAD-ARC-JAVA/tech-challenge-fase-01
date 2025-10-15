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

/**
 * Mapper para converter entre DTOs da camada web e DTOs da camada de aplicação.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Converter DTOs de entrada da web para DTOs da aplicação
 * - Converter DTOs de saída da aplicação para DTOs da web
 * 
 * Arquitetura Hexagonal:
 * - Faz parte do Adapter Inbound (web)
 * - Isola a camada web da camada de aplicação
 * - Permite que cada camada tenha seus próprios DTOs com necessidades
 * específicas
 */
@Component
public class UserWebMapper {

    /**
     * Converte DTO de criação de usuário da web para aplicação.
     */
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

    /**
     * Converte DTO de atualização de usuário da web para aplicação.
     */
    public UserUpdateRequest toApplicationUpdateRequest(UserUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserUpdateRequest(
                dto.name(),
                dto.email(),
                dto.login());
    }

    /**
     * Converte DTO de atualização de senha da web para aplicação.
     */
    public UpdatePasswordRequest toApplicationPasswordRequest(UpdatePasswordRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UpdatePasswordRequest(
                dto.currentPassword(),
                dto.newPassword(),
                dto.confirmPassword());
    }

    /**
     * Converte resposta da aplicação para resposta web.
     */
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
