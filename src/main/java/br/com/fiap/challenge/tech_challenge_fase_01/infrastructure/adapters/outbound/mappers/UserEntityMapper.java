package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;

@Component
public class UserEntityMapper {

    public JpaUserEntity toEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setActive(domain.isActive());

        if (domain.getRole() != null) {
            List<RolesEnum> entityRoles = domain
                    .getRole().stream()
                    .map(this::toEntityRole)
                    .collect(Collectors.toList());
            entity.setRole(entityRoles);
        }

        return entity;
    }

    public UserDomain toDomain(JpaUserEntity entity) {
        if (entity == null) {
            return null;
        }

        List<RolesEnum> domainRoles = null;
        if (entity.getRole() != null) {
            domainRoles = entity.getRole().stream()
                    .map(this::toDomainRole)
                    .collect(Collectors.toList());
        }

        return UserDomain.builder()
                .id(entity.getId())
                .name(entity.getName() != null ? PersonName.of(entity.getName()) : null)
                .email(entity.getEmail() != null ? Email.of(entity.getEmail()) : null)
                .login(entity.getLogin() != null ? Username.of(entity.getLogin()) : null)
                .password(entity.getPassword())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .role(domainRoles)
                .isActive(entity.isActive())
                .build();
    }

    private RolesEnum toEntityRole(
            RolesEnum domainRole) {
        if (domainRole == null) {
            return null;
        }
        return RolesEnum
                .valueOf(domainRole.name());
    }

    private RolesEnum toDomainRole(
            RolesEnum entityRole) {
        if (entityRole == null) {
            return null;
        }
        return RolesEnum.valueOf(entityRole.name());
    }
}