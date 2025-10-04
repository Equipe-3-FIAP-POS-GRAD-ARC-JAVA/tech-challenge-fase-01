package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = { RolesMapper.class },
        implementationName = "OutboundUserMapperImpl"
)
public interface UserMapper {

    // Domain -> Entity
    @Mapping(target = "id",       source = "id", qualifiedByName = "stringToUuid")
    @Mapping(target = "active", source = "active")
    // roles: o MapStruct usa RolesMapper.toEntityList(List<RolesEnum>) automaticamente
    JpaUserEntity toEntity(User source);

    // Entity -> Domain
    @Mapping(target = "id",       source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "isActive", source = "active")
    // roles: o MapStruct usa RolesMapper.toDomainList(List<RolesEnumEntity>) automaticamente
    User toDomain(JpaUserEntity source);

    @Named("stringToUuid")
    default UUID stringToUuid(String id) {
        return (id == null || id.isBlank()) ? null : UUID.fromString(id);
    }

    @Named("uuidToString")
    default String uuidToString(UUID id) {
        return id == null ? null : id.toString();
    }
}
