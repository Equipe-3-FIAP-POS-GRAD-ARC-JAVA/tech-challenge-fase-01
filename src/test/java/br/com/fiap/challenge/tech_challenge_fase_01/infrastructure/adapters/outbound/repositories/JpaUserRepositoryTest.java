package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.user.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository repository;

    @Test
    void shouldSaveAndFindByLoginIgnoreCase(){
        JpaUserEntity e = new JpaUserEntity();
        e.setName("Maria");
        e.setEmail("maria@example.com");
        e.setLogin("maria");
        e.setPassword("hash");
        e.setRole(List.of(RolesEnum.CLIENT));
        e.setActive(true);

        repository.save(e);

        assertTrue(repository.findByLoginIgnoreCase("MARIA").isPresent());
        assertTrue(repository.findByEmailIgnoreCase("MARIA@EXAMPLE.COM").isPresent());
    }

    @Test
    void findByName_shouldReturnOnlyActiveUsers(){
        JpaUserEntity a = new JpaUserEntity();
        a.setName("Carlos Silva");
        a.setEmail("c1@example.com");
        a.setLogin("c1");
        a.setPassword("h");
        a.setRole(List.of(RolesEnum.CLIENT));
        a.setActive(true);

        JpaUserEntity b = new JpaUserEntity();
        b.setName("Carlos Souza");
        b.setEmail("c2@example.com");
        b.setLogin("c2");
        b.setPassword("h");
        b.setRole(List.of(RolesEnum.CLIENT));
        b.setActive(false);

        repository.saveAll(List.of(a,b));

        var result = repository.findByName("Carlos");
        assertEquals(1, result.size());
        assertEquals("Carlos Silva", result.get(0).getName());
    }
}
