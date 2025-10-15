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

/**
 * Mapper para converter entre UserDomain (aplicação) e JpaUserEntity
 * (persistência).
 * 
 * Responsabilidade (SOLID - SRP):
 * - Converter objetos de domínio para entidades JPA e vice-versa
 * - Converter enums entre camadas
 * - Lidar com Value Objects do domínio
 * 
 * Arquitetura Hexagonal:
 * - Faz parte do Adapter Outbound (persistência)
 * - Isola o domínio dos detalhes de persistência
 * - Permite que o domínio seja agnóstico de JPA
 */
@Component
public class UserEntityMapper {

    /**
     * Converte UserDomain para JpaUserEntity.
     */
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

        // Converte roles do domínio para roles da entidade
        if (domain.getRole() != null) {
            List<RolesEnum> entityRoles = domain
                    .getRole().stream()
                    .map(this::toEntityRole)
                    .collect(Collectors.toList());
            entity.setRole(entityRoles);
        }

        return entity;
    }

    /**
     * Converte JpaUserEntity para UserDomain usando builder pattern.
     */
    public UserDomain toDomain(JpaUserEntity entity) {
        if (entity == null) {
            return null;
        }

        // Converte roles da entidade para roles do domínio
        List<RolesEnum> domainRoles = null;
        if (entity.getRole() != null) {
            domainRoles = entity.getRole().stream()
                    .map(this::toDomainRole)
                    .collect(Collectors.toList());
        }

        // Usa builder do UserDomain
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

    /**
     * Converte role do domínio para role da entidade.
     */
    private RolesEnum toEntityRole(
            RolesEnum domainRole) {
        if (domainRole == null) {
            return null;
        }
        return RolesEnum
                .valueOf(domainRole.name());
    }

    /**
     * Converte role da entidade para role do domínio.
     */
    private RolesEnum toDomainRole(
            RolesEnum entityRole) {
        if (entityRole == null) {
            return null;
        }
        return RolesEnum.valueOf(entityRole.name());
    }
}
