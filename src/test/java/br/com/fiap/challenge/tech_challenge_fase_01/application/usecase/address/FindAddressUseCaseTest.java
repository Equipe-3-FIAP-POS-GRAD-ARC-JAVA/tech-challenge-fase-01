package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

@ExtendWith(MockitoExtension.class)
class FindAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    private FindAddressUseCase findAddressUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID addressId1 = UUID.randomUUID();
    private final UUID addressId2 = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        findAddressUseCase = new FindAddressUseCase(addressRepository);
    }

    @Test
    @DisplayName("Deve encontrar endereços do usuário com sucesso")
    void shouldFindUserAddressesSuccessfully() {
        // Given
        AddressDomain address1 = AddressDomain.builder()
            .id(addressId1)
            .userId(userId)
            .street("Rua das Flores")
            .number("123")
            .complement("Apto 45")
            .neighborhood("Centro")
            .city("São Paulo")
            .zipCode("01234567")
            .build();

        AddressDomain address2 = AddressDomain.builder()
            .id(addressId2)
            .userId(userId)
            .street("Rua das Palmeiras")
            .number("456")
            .complement(null)
            .neighborhood("Jardim")
            .city("Rio de Janeiro")
            .zipCode("87654321")
            .build();

        List<AddressDomain> addresses = Arrays.asList(address1, address2);

        when(addressRepository.findByAddressFromUser(userId)).thenReturn(addresses);

        // When
        List<AddressResponse> responses = findAddressUseCase.findAddress(userId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());

        AddressResponse response1 = responses.get(0);
        assertEquals(addressId1, response1.id());
        assertEquals(userId, response1.userId());
        assertEquals("Rua das Flores", response1.street());
        assertEquals("123", response1.number());
        assertEquals("Apto 45", response1.complement());
        assertEquals("Centro", response1.neighborhood());
        assertEquals("São Paulo", response1.city());
        assertEquals("01234567", response1.zipCode());

        AddressResponse response2 = responses.get(1);
        assertEquals(addressId2, response2.id());
        assertEquals(userId, response2.userId());
        assertEquals("Rua das Palmeiras", response2.street());
        assertEquals("456", response2.number());
        assertNull(response2.complement());
        assertEquals("Jardim", response2.neighborhood());
        assertEquals("Rio de Janeiro", response2.city());
        assertEquals("87654321", response2.zipCode());

        verify(addressRepository, times(1)).findByAddressFromUser(userId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tem endereços")
    void shouldReturnEmptyListWhenUserHasNoAddresses() {
        // Given
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of());

        // When
        List<AddressResponse> responses = findAddressUseCase.findAddress(userId);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(addressRepository, times(1)).findByAddressFromUser(userId);
    }

    @Test
    @DisplayName("Deve encontrar endereços com diferentes complementos")
    void shouldFindAddressesWithDifferentComplements() {
        // Given
        AddressDomain addressWithComplement = AddressDomain.builder()
            .id(addressId1)
            .userId(userId)
            .street("Rua A")
            .number("1")
            .complement("Casa")
            .neighborhood("Bairro A")
            .city("Cidade A")
            .zipCode("12345678")
            .build();

        AddressDomain addressWithoutComplement = AddressDomain.builder()
            .id(addressId2)
            .userId(userId)
            .street("Rua B")
            .number("2")
            .complement(null)
            .neighborhood("Bairro B")
            .city("Cidade B")
            .zipCode("87654321")
            .build();

        List<AddressDomain> addresses = Arrays.asList(addressWithComplement, addressWithoutComplement);

        when(addressRepository.findByAddressFromUser(userId)).thenReturn(addresses);

        // When
        List<AddressResponse> responses = findAddressUseCase.findAddress(userId);

        // Then
        assertEquals(2, responses.size());
        assertEquals("Casa", responses.get(0).complement());
        assertNull(responses.get(1).complement());
        verify(addressRepository, times(1)).findByAddressFromUser(userId);
    }
}