package br.com.fiap.challenge.tech_challenge_fase_01.support;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;


public final class TestFixtures {

    private TestFixtures(){}

    public static UserDomain userDomain(UUID id){
        String uniq = UUID.randomUUID().toString().substring(0, 8);

        var builder = UserDomain.builder()
                .name(PersonName.of("John Doe"))
                .email(Email.of("john.doe+" + uniq + "@example.com"))
                .login(Username.of("johndoe_" + uniq))
                .password("P@ssw0rd")
                .role(Collections.singletonList(RolesEnum.CLIENT))
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now());

        if (id != null) {
            builder.id(id);
        }

        return builder.build();
    }

    public static JpaUserEntity jpaUser(UUID id){
        String uniq = UUID.randomUUID().toString().substring(0, 8);

        JpaUserEntity e = new JpaUserEntity();
        if (id != null) e.setId(id);
        e.setName("John Doe");
        e.setEmail("john.doe+" + uniq + "@example.com");
        e.setLogin("johndoe_" + uniq);
        e.setPassword("P@ssw0rd");
        e.setRole(java.util.List.of(RolesEnum.CLIENT));
        e.setActive(true);
        e.setCreatedAt(LocalDateTime.now().minusDays(1));
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }

    public static AddressDomain addressDomain(UUID id, UUID userId){
        if (userId == null) userId = UUID.randomUUID();

        var builder = AddressDomain.builder()
                .userId(userId)
                .street("Rua Alpha")
                .number("123")
                .city("São Paulo")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now());

        if (id != null) {
            builder.id(id);
        }

        return builder.build();
    }


    public static JpaAddressEntity jpaAddress(UUID id, UUID userId){
        if(id == null) id = UUID.randomUUID();
        JpaAddressEntity e = new JpaAddressEntity();
        e.setId(id);
        e.setStreet("Rua Alpha");
        e.setNumber("123");
        e.setCity("São Paulo");
        e.setCreatedAt(LocalDateTime.now().minusDays(1));
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }
}