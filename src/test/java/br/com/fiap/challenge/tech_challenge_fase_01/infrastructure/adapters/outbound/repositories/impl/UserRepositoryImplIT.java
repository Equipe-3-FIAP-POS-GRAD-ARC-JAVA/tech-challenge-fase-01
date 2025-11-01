package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.impl;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.support.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryImplIT {

    @Autowired
    private UserRepositoryPort repository;

    @Test
    void saveFindUpdateDeleteFlow(){
        UserDomain domain = TestFixtures.userDomain(null);
        UserDomain saved = repository.save(domain);
        assertNotNull(saved.getId());

        var found = repository.findById(saved.getId()).orElseThrow();
        assertEquals(saved.getLogin(), found.getLogin());

        repository.delete(saved.getId());

        assertThrows(UserNotFoundException.class, () -> repository.findByName(saved.getName().toString()));
    }

    @Test
    void existsByUsername_only(){
        UserDomain domain = TestFixtures.userDomain(null);
        repository.save(domain);

        assertTrue(repository.existsByUsername(domain.getLogin()));
    }
}
