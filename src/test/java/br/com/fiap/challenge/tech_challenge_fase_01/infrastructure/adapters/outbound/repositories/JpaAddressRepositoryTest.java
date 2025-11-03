package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.address.JpaAddressRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.user.JpaUserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class JpaAddressRepositoryTest {

    @Autowired
    private JpaAddressRepository addressRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    void shouldSaveAndFetchByUserOrdered(){
        JpaUserEntity user = new JpaUserEntity();
        user.setName("Maria");
        user.setEmail("maria@example.com");
        user.setLogin("maria");
        user.setPassword("hash");
        user.setRole(java.util.List.of(RolesEnum.CLIENT));
        user.setActive(true);
        user = userRepository.save(user);

        JpaAddressEntity older = new JpaAddressEntity();
        older.setStreet("A");
        older.setNumber("1");
        older.setCity("SP");
        older.setUser(user);

        JpaAddressEntity newer = new JpaAddressEntity();
        newer.setStreet("B");
        newer.setNumber("2");
        newer.setCity("SP");
        newer.setUser(user);

        addressRepository.saveAll(List.of(older, newer));

        var last = addressRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId()).orElseThrow();
        assertEquals("B", last.getStreet());

        var all = addressRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        assertEquals(2, all.size());
        assertEquals("B", all.get(0).getStreet());
    }
}
