package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.address.JpaAddressRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.user.JpaUserRepository;

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
        older.setComplement("Casa");
        older.setNeighborhood("Centro");
        older.setCity("SP");
        older.setZipCode("01234-567");
        older.setUser(user);

        JpaAddressEntity newer = new JpaAddressEntity();
        newer.setStreet("B");
        newer.setNumber("2");
        newer.setComplement("Apt 2");
        newer.setNeighborhood("Vila Nova");
        newer.setCity("SP");
        newer.setZipCode("05678-901");
        newer.setUser(user);

        addressRepository.saveAll(List.of(older, newer));

        var last = addressRepository.findTopByUser_IdOrderByCreatedAtDesc(user.getId()).orElseThrow();
        assertEquals("B", last.getStreet());

        var all = addressRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId());
        assertEquals(2, all.size());
        assertEquals("B", all.get(0).getStreet());
    }
}
