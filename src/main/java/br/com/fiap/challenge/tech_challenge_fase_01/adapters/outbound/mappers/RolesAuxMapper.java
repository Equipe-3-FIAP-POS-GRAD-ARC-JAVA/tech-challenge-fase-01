package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.enumx.RolesEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RolesAuxMapper {

    @Named("stringToEnumList")
    default List<RolesEnum> stringToEnumList(String role) {
        if (role == null || role.isBlank()) return new ArrayList<>();
        return List.of(RolesEnum.valueOf(role.trim().toUpperCase()));
    }

    @Named("enumToStringList")
    default String enumToStringList(List<RolesEnum> roles) {
        if (roles == null || roles.isEmpty()) return null;
        return roles.get(0).name(); // Se quiser só o primeiro
        // return String.join(",", roles.stream().map(Enum::name).toList()); // Se quiser todos juntos
    }
}
