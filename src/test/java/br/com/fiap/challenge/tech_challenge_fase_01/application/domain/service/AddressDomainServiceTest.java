package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddresDoesNotBelongToUserException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.AddressNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressDomainService Tests")
class AddressDomainServiceTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private AddressDomainService addressDomainService;

    private AddressDomain createValidAddress(UUID userId, UUID addressId) {
        return AddressDomain.builder()
                .id(addressId)
                .userId(userId)
                .street("Rua das Flores")
                .number("123")
                .neighborhood("Centro")
                .city("São Paulo")
                .zipCode("01234-567")
                .build();
    }

    @Nested
    @DisplayName("Address Ownership Tests")
    class AddressOwnershipTests {

        @Test
        @DisplayName("Should pass when address belongs to user")
        void shouldPassWhenAddressBelongsToUser() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();
            AddressDomain address = createValidAddress(userId, addressId);
            
            when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

            // When & Then
            addressDomainService.ensureAddressBelongsToUser(userId, addressId);
            
            verify(addressRepository).findById(addressId);
        }

        @Test
        @DisplayName("Should throw exception when address not found")
        void shouldThrowExceptionWhenAddressNotFound() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();
            
            when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

            // When & Then
            AddressNotFoundException exception = assertThrows(
                    AddressNotFoundException.class,
                    () -> addressDomainService.ensureAddressBelongsToUser(userId, addressId)
            );
            
            assertEquals("Endereço não encontrado: " + addressId, exception.getMessage());
            verify(addressRepository).findById(addressId);
        }

        @Test
        @DisplayName("Should throw exception when address does not belong to user")
        void shouldThrowExceptionWhenAddressDoesNotBelongToUser() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID differentUserId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();
            AddressDomain address = createValidAddress(differentUserId, addressId); // Address belongs to different user
            
            when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

            // When & Then
            AddresDoesNotBelongToUserException exception = assertThrows(
                    AddresDoesNotBelongToUserException.class,
                    () -> addressDomainService.ensureAddressBelongsToUser(userId, addressId)
            );
            
            assertEquals("O endereço " + addressId + " não pertence ao usuário " + userId + ".", exception.getMessage());
            verify(addressRepository).findById(addressId);
        }

        @Test
        @DisplayName("Should handle null user ID gracefully")
        void shouldHandleNullUserIdGracefully() {
            // Given
            UUID addressId = UUID.randomUUID();
            UUID realUserId = UUID.randomUUID();
            AddressDomain address = createValidAddress(realUserId, addressId);
            
            when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

            // When & Then
            AddresDoesNotBelongToUserException exception = assertThrows(
                    AddresDoesNotBelongToUserException.class,
                    () -> addressDomainService.ensureAddressBelongsToUser(null, addressId)
            );
            
            assertEquals("O endereço " + addressId + " não pertence ao usuário " + null + ".", exception.getMessage());
            verify(addressRepository).findById(addressId);
        }

        @Test
        @DisplayName("Should handle null address ID gracefully")
        void shouldHandleNullAddressIdGracefully() {
            // Given
            UUID userId = UUID.randomUUID();
            
            when(addressRepository.findById(null)).thenReturn(Optional.empty());

            // When & Then
            AddressNotFoundException exception = assertThrows(
                    AddressNotFoundException.class,
                    () -> addressDomainService.ensureAddressBelongsToUser(userId, null)
            );
            
            assertEquals("Endereço não encontrado: " + null, exception.getMessage());
            verify(addressRepository).findById(null);
        }
    }
}