package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.AddressDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

@ExtendWith(MockitoExtension.class)
class DeleteAddressUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private AddressDomainService addressDomainService;

    private DeleteAddressUseCase deleteAddressUseCase;

    private final UUID userId = UUID.randomUUID();
    private final UUID addressId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        deleteAddressUseCase = new DeleteAddressUseCase(addressRepository, addressDomainService);
    }

    @Test
    @DisplayName("Deve deletar endereço com sucesso")
    void shouldDeleteAddressSuccessfully() {
        // Given
        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        doNothing().when(addressRepository).deleteAddress(addressId);

        // When
        assertDoesNotThrow(() -> deleteAddressUseCase.deleteAddress(userId, addressId));

        // Then
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, times(1)).deleteAddress(addressId);
    }

    @Test
    @DisplayName("Deve propagar exceção quando endereço não pertencer ao usuário")
    void shouldPropagateExceptionWhenAddressDoesNotBelongToUser() {
        // Given
        doThrow(new RuntimeException("Endereço não pertence ao usuário"))
            .when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            deleteAddressUseCase.deleteAddress(userId, addressId)
        );

        assertEquals("Endereço não pertence ao usuário", exception.getMessage());
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, never()).deleteAddress(addressId);
    }

    @Test
    @DisplayName("Deve propagar exceção quando endereço não for encontrado")
    void shouldPropagateExceptionWhenAddressNotFound() {
        // Given
        doThrow(new RuntimeException("Endereço não encontrado"))
            .when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            deleteAddressUseCase.deleteAddress(userId, addressId)
        );

        assertEquals("Endereço não encontrado", exception.getMessage());
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(userId, addressId);
        verify(addressRepository, never()).deleteAddress(addressId);
    }

    @Test
    @DisplayName("Deve chamar repositório para deletar endereço após validação")
    void shouldCallRepositoryToDeleteAddressAfterValidation() {
        // Given
        doNothing().when(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        doNothing().when(addressRepository).deleteAddress(addressId);

        // When
        deleteAddressUseCase.deleteAddress(userId, addressId);

        // Then
        // Verificar ordem de chamadas
        var inOrder = inOrder(addressDomainService, addressRepository);
        inOrder.verify(addressDomainService).ensureAddressBelongsToUser(userId, addressId);
        inOrder.verify(addressRepository).deleteAddress(addressId);
    }

    @Test
    @DisplayName("Deve funcionar com diferentes IDs de usuário e endereço")
    void shouldWorkWithDifferentUserAndAddressIds() {
        // Given
        UUID differentUserId = UUID.randomUUID();
        UUID differentAddressId = UUID.randomUUID();
        
        doNothing().when(addressDomainService).ensureAddressBelongsToUser(differentUserId, differentAddressId);
        doNothing().when(addressRepository).deleteAddress(differentAddressId);

        // When
        deleteAddressUseCase.deleteAddress(differentUserId, differentAddressId);

        // Then
        verify(addressDomainService, times(1)).ensureAddressBelongsToUser(differentUserId, differentAddressId);
        verify(addressRepository, times(1)).deleteAddress(differentAddressId);
    }
}