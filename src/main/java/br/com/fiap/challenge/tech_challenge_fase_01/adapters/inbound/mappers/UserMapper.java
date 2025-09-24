package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserResponseDTO toResponse(User user);

    public abstract User toDomainCreate(UserCreateRequestDTO userCreateRequestDTO);

    public abstract User toDomainUpdate(UserUpdateRequestDTO userUpdateRequestDTO);

    public abstract User toDomainUpdatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO);

}