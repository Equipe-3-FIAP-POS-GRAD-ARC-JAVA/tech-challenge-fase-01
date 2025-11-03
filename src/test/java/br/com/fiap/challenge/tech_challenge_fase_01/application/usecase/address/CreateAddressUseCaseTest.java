package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CreateAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    private CreateAddressUseCase createAddressUseCase;

    private final UUID userId = UUID.randomUUID();
    private final String street = "Rua das Flores";
    private final String number = "123";
    private final String complement = "Apto 45";
    private final String neighborhood = "Centro";
    private final String city = "São Paulo";
    private final String zipCode = "01234567";

    @BeforeEach
    void setUp() {
        createAddressUseCase = new CreateAddressUseCase(addressRepository);
    }

    @Test
    @DisplayName("Deve criar endereço com sucesso")
    void shouldCreateAddressSuccessfully() {
        // Given
        AddressCreateRequest request = new AddressCreateRequest(
            userId, street, number, complement, neighborhood, city, zipCode
        );

        AddressDomain savedAddress = AddressDomain.create(
            userId, street, number, complement, neighborhood, city, zipCode
        );

        when(addressRepository.save(any(AddressDomain.class))).thenReturn(savedAddress);

        // When
        AddressResponse response = createAddressUseCase.createAddress(request);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals(street, response.street());
        assertEquals(number, response.number());
        assertEquals(complement, response.complement());
        assertEquals(neighborhood, response.neighborhood());
        assertEquals(city, response.city());
        assertEquals(zipCode, response.zipCode());

        verify(addressRepository, times(1)).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve criar endereço sem complemento")
    void shouldCreateAddressWithoutComplement() {
        // Given
        AddressCreateRequest request = new AddressCreateRequest(
            userId, street, number, null, neighborhood, city, zipCode
        );

        AddressDomain savedAddress = AddressDomain.create(
            userId, street, number, null, neighborhood, city, zipCode
        );

        when(addressRepository.save(any(AddressDomain.class))).thenReturn(savedAddress);

        // When
        AddressResponse response = createAddressUseCase.createAddress(request);

        // Then
        assertNotNull(response);
        assertNull(response.complement());
        verify(addressRepository, times(1)).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve propagar exceção de validação do domínio")
    void shouldPropagateValidationException() {
        // Given
        AddressCreateRequest request = new AddressCreateRequest(
            null, street, number, complement, neighborhood, city, zipCode
        );

        // When & Then
        assertThrows(Exception.class, () -> createAddressUseCase.createAddress(request));
        
        verify(addressRepository, never()).save(any(AddressDomain.class));
    }
}