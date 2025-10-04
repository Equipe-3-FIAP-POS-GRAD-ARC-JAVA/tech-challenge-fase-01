package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {RolesAuxMapper.class},
        implementationName = "OutboundUserMapperImpl"
)
public interface UserMapper {

    @Mapping(target = "roles", source = "role", qualifiedByName = "stringToEnumList")
    JpaUserEntity toEntity(User source);

    @Mapping(target = "roles", source = "role", qualifiedByName = "enumToStringList")
    User toDomain(JpaUserEntity source);
}
