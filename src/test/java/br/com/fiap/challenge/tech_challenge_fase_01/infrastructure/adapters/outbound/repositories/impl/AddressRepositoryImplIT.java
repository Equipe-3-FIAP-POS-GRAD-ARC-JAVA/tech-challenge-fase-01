package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.support.TestFixtures;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AddressRepositoryImplIT {

    @Autowired
    private AddressRepositoryPort addressRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    private UserDomain savedUser;

    @BeforeEach
    void setUp() {
        // Preparar um usuário para os testes usando TestFixtures
        UserDomain userDomain = TestFixtures.userDomain(null);
        savedUser = userRepository.save(userDomain);
    }

    @Test
    void shouldSaveAddressAndRetrieveByUserId() {
        // given
        AddressDomain addressDomain = TestFixtures.addressDomain(null, savedUser.getId());

        // when
        AddressDomain savedAddress = addressRepository.save(addressDomain);
        List<AddressDomain> addresses = addressRepository.findByAddressFromUser(savedUser.getId());

        // then
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getUserId()).isEqualTo(savedUser.getId());
        assertThat(savedAddress.getStreet()).isEqualTo("Rua Alpha");
        assertThat(savedAddress.getNumber()).isEqualTo("123");
        assertThat(savedAddress.getNeighborhood()).isEqualTo("Centro");
        assertThat(savedAddress.getCity()).isEqualTo("São Paulo");
        assertThat(savedAddress.getZipCode()).isEqualTo("01234567");

        assertThat(addresses)
                .hasSize(1)
                .first()
                .satisfies(address -> {
                    assertThat(address.getId()).isEqualTo(savedAddress.getId());
                    assertThat(address.getUserId()).isEqualTo(savedUser.getId());
                    assertThat(address.getStreet()).isEqualTo("Rua Alpha");
                });
    }

    @Test
    void shouldSaveMultipleAddressesForSameUser() {
        // given
        AddressDomain address1 = TestFixtures.addressDomain(null, savedUser.getId());
        AddressDomain address2 = TestFixtures.addressDomain(null, savedUser.getId());

        // when
        AddressDomain savedAddress1 = addressRepository.save(address1);
        AddressDomain savedAddress2 = addressRepository.save(address2);
        List<AddressDomain> addresses = addressRepository.findByAddressFromUser(savedUser.getId());

        // then
        assertThat(savedAddress1).isNotNull();
        assertThat(savedAddress1.getId()).isNotNull();
        assertThat(savedAddress2).isNotNull();
        assertThat(savedAddress2.getId()).isNotNull();

        assertThat(addresses)
                .hasSize(2)
                .extracting(AddressDomain::getId)
                .containsExactlyInAnyOrder(savedAddress1.getId(), savedAddress2.getId());
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoAddresses() {
        // given
        UserDomain anotherUser = TestFixtures.userDomain(null);
        UserDomain savedAnotherUser = userRepository.save(anotherUser);

        // when
        List<AddressDomain> addresses = addressRepository.findByAddressFromUser(savedAnotherUser.getId());

        // then
        assertThat(addresses).isEmpty();
    }

    @Test
    void shouldReturnEmptyListForNonExistentUser() {
        // given
        UUID nonExistentUserId = UUID.randomUUID();

        // when
        List<AddressDomain> addresses = addressRepository.findByAddressFromUser(nonExistentUserId);

        // then
        assertThat(addresses).isEmpty();
    }

    @Test
    void saveAndQueryByUser() {
        // Mantendo o teste original corrigido
        AddressDomain a = TestFixtures.addressDomain(null, savedUser.getId());
        AddressDomain b = TestFixtures.addressDomain(null, savedUser.getId());

        AddressDomain savedA = addressRepository.save(a);
        AddressDomain savedB = addressRepository.save(b);

        List<AddressDomain> byUser = addressRepository.findByAddressFromUser(savedUser.getId());
        assertEquals(2, byUser.size());
        assertTrue(addressRepository.findById(savedA.getId()).isPresent());
        assertTrue(addressRepository.findById(savedB.getId()).isPresent());
    }
}
