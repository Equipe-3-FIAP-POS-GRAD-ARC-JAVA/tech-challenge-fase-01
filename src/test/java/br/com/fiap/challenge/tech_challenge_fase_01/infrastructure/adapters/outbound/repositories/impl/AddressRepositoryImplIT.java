package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.impl;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.support.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AddressRepositoryImplIT {

    @Autowired
    private AddressRepositoryPort addressRepository;

    @Autowired
    private UserRepositoryPort userRepository;

    @Test
    void saveAndQueryByUser(){

        UserDomain user = UserDomain.builder()
                .name(PersonName.of("Addr Owner"))
                .email(Email.of("owner+" + UUID.randomUUID().toString().substring(0,8) + "@example.com"))
                .login(Username.of("owner_" + UUID.randomUUID().toString().substring(0,8)))
                .password("P@ssw0rd")
                .role(Collections.singletonList(RolesEnum.CLIENT))
                .build();
        user = userRepository.save(user);
        UUID userId = user.getId();

        AddressDomain a = TestFixtures.addressDomain(null, userId);
        AddressDomain b = TestFixtures.addressDomain(null, userId);

        addressRepository.save(a);
        addressRepository.save(b);

        List<AddressDomain> byUser = addressRepository.findByAddressFromUser(userId);
        assertEquals(2, byUser.size());
        assertTrue(addressRepository.findById(byUser.get(0).getId()).isPresent());
    }
}
