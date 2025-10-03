package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "OutboundUserMapperImpl")
public interface UserMapper {

    // Domain -> Entity
//    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "id",         source = "id"),
//            @Mapping(target = "name",       source = "name"),
//            @Mapping(target = "email",      source = "email"),
//            @Mapping(target = "login",      source = "login"),
//            @Mapping(target = "password",   source = "password"),
//            @Mapping(target = "createdAt",  source = "createdAt"),
//            @Mapping(target = "updatedAt",  source = "updatedAt"),
//            @Mapping(target = "role",       source = "role"),
//            @Mapping(target = "active",     source = "active")
//    })
    JpaUserEntity toEntity(User source);

    // Entity -> Domain
//    @BeanMapping(ignoreByDefault = true)
//    @Mappings({
//            @Mapping(target = "id",         source = "id"),
//            @Mapping(target = "name",       source = "name"),
//            @Mapping(target = "email",      source = "email"),
//            @Mapping(target = "login",      source = "login"),
//            @Mapping(target = "password",   source = "password"),
//            @Mapping(target = "createdAt",  source = "createdAt"),
//            @Mapping(target = "updatedAt",  source = "updatedAt"),
//            @Mapping(target = "role",       source = "role"),
//            //@Mapping(target = "active",     source = "active")
//    })
    User toDomain(JpaUserEntity source);
}
