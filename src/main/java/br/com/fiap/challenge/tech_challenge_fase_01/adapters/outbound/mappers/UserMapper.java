package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    // Aqui o MapStruct consegue gerar tudo via setters do JpaUserEntity.
    @Mapping(target = "id",        source = "id")
    @Mapping(target = "name",      source = "name")
    @Mapping(target = "email",     source = "email")
    @Mapping(target = "login",     source = "login")
    @Mapping(target = "password",  source = "password")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "role",      source = "role")
    @Mapping(target = "isActive",  source = "active") // Lombok expõe getIsActive() como isActive()
    public abstract JpaUserEntity toEntity(User user);

    // Assinatura "normal": o MapStruct chamará o @ObjectFactory para criar o User.
    public abstract User toDomain(JpaUserEntity entity);