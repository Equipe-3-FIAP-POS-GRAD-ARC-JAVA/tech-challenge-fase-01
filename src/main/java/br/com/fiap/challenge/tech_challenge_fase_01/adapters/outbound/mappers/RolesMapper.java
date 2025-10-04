package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.enumx.RolesEnumEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.RolesEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RolesMapper {

    // item a item
    @Named("toDomainRole")
    RolesEnum toDomainRole(RolesEnumEntity role);

    @Named("toEntityRole")
    RolesEnumEntity toEntityRole(RolesEnum role);

    // listas (o MapStruct consegue gerar a partir dos métodos de item; manter explícito ajuda a leitura)
    List<RolesEnum> toDomainList(List<RolesEnumEntity> rolesEntityList);
    List<RolesEnumEntity> toEntityList(List<RolesEnum> roleList);
}
