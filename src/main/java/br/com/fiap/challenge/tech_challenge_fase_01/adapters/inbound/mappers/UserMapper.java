package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", implementationName = "InboundUserMapperImpl")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "id",    source = "id"),
//            @Mapping(target = "name",  source = "name"),
//            @Mapping(target = "email", source = "email"),
//            @Mapping(target = "login", source = "login")
//    })
    UserResponseDTO toResponse(User user);

    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "name",     source = "name"),
//            @Mapping(target = "email",    source = "email"),
//            @Mapping(target = "login",    source = "login"),
//            @Mapping(target = "password", source = "password")
//    })
    User toDomainCreate(UserCreateRequestDTO dto);

    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "name",  source = "name"),
//            @Mapping(target = "email", source = "email"),
//            @Mapping(target = "login", source = "login")
//    })
    User toDomainUpdate(UserUpdateRequestDTO dto);

    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "id",       source = "id"),
//            @Mapping(target = "password", source = "password")
//    })
    User toDomainUpdatePassword(UpdatePasswordRequestDTO dto);
}
