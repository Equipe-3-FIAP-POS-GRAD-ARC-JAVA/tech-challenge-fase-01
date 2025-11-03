package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.AddressDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddressNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UpdateAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private AddressDomainService addressDomainService;

    private UpdateAddressUseCase updateAddressUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID addressId = UUID.randomUUID();
    private final String street = "Rua das Flores";
    private final String number = "123";
    private final String complement = "Apto 45";
    private final String neighborhood = "Centro";
    private final String city = "São Paulo";
    private final String zipCode = "01234567";

    @BeforeEach
    void setUp() {
        updateAddressUseCase = new UpdateAddressUseCase(addressRepository, addressDomainService);
    }

    @Test
    @DisplayName("Deve atualizar endereço com sucesso")
    void shouldUpdateAddressSuccessfully() {
        // Given
        AddressDomain existingAddress = AddressDomain.builder()
            .id(addressId)
            .userId(userId)
            .street("Rua Antiga")
            .number("100")
            .complement("Casa")
            .neighborhood("Bairro Antigo")
            .city("Cidade Antiga")
            .zipCode("11111111")
            .createdAt(LocalDateTime.now().minusDays(1))
            .build();

        AddressUpdateRequest request = new AddressUpdateRequest(
            street, number, complement, neighborhood, city, zipCode
        );

        AddressDomain updatedAddress = AddressDomain.builder()
            .id(addressId)
            .userId(userId)
            .street(street)
            .number(number)
            .complement(complement)
            .neighborhood(neighborhood)
            .city(city)
            .zipCode(zipCode)
            .createdAt(existingAddress.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(AddressDomain.class))).thenReturn(updatedAddress);

        // When
        AddressResponse response = updateAddressUseCase.updateAddress(userId, addressId, request);

        // Then
        assertNotNull(response);
        assertEquals(addressId, response.id());
        assertEquals(userId, response.userId());
        assertEquals(street, response.street());
        assertEquals(number, response.number());
        assertEquals(complement, response.complement());
        assertEquals(neighborhood, response.neighborhood());
        assertEquals(city, response.city());
        assertEquals(zipCode, response.zipCode());

        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve atualizar endereço sem complemento")
    void shouldUpdateAddressWithoutComplement() {
        // Given
        AddressDomain existingAddress = AddressDomain.builder()
            .id(addressId)
            .userId(userId)
            .street("Rua Antiga")
            .number("100")
            .complement("Casa")
            .neighborhood("Bairro Antigo")
            .city("Cidade Antiga")
            .zipCode("11111111")
            .createdAt(LocalDateTime.now().minusDays(1))
            .build();

        AddressUpdateRequest request = new AddressUpdateRequest(
            street, number, null, neighborhood, city, zipCode
        );

        AddressDomain updatedAddress = AddressDomain.builder()
            .id(addressId)
            .userId(userId)
            .street(street)
            .number(number)
            .complement(null)
            .neighborhood(neighborhood)
            .city(city)
            .zipCode(zipCode)
            .createdAt(existingAddress.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(AddressDomain.class))).thenReturn(updatedAddress);

        // When
        AddressResponse response = updateAddressUseCase.updateAddress(userId, addressId, request);

        // Then
        assertNotNull(response);
        assertNull(response.complement());
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando endereço não for encontrado")
    void shouldThrowExceptionWhenAddressNotFound() {
        // Given
        AddressUpdateRequest request = new AddressUpdateRequest(
            street, number, complement, neighborhood, city, zipCode
        );

        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // When & Then
        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () ->
            updateAddressUseCase.updateAddress(userId, addressId, request)
        );

        assertTrue(exception.getMessage().contains("Endereço não encontrado com ID: " + addressId));
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve propagar exceção quando endereço não pertencer ao usuário")
    void shouldPropagateExceptionWhenAddressDoesNotBelongToUser() {
        // Given
        AddressUpdateRequest request = new AddressUpdateRequest(
            street, number, complement, neighborhood, city, zipCode
        );

        doThrow(new RuntimeException("Endereço não pertence ao usuário"))
            .when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            updateAddressUseCase.updateAddress(userId, addressId, request)
        );

        assertEquals("Endereço não pertence ao usuário", exception.getMessage());
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, never()).findById(addressId);
        verify(addressRepository, never()).save(any(AddressDomain.class));
    }

    @Test
    @DisplayName("Deve propagar exceção de validação do domínio")
    void shouldPropagateValidationException() {
        // Given
        AddressDomain existingAddress = AddressDomain.builder()
            .id(addressId)
            .userId(userId)
            .street("Rua Antiga")
            .number("100")
            .complement("Casa")
            .neighborhood("Bairro Antigo")
            .city("Cidade Antiga")
            .zipCode("11111111")
            .createdAt(LocalDateTime.now().minusDays(1))
            .build();

        AddressUpdateRequest request = new AddressUpdateRequest(
            null, number, complement, neighborhood, city, zipCode // street null
        );

        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));

        // When & Then
        assertThrows(Exception.class, () ->
            updateAddressUseCase.updateAddress(userId, addressId, request)
        );

        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).save(any(AddressDomain.class));
    }
}