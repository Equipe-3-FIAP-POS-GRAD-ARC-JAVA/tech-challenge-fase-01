package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract JpaUserEntity toEntity(User user);

    public abstract User toDomain(JpaUserEntity entity);
}