package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    private UUID userId;
    private UserDomain userDomain;
    private AddressDomain addressDomain;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        userDomain = UserDomain.builder()
            .id(userId)
            .name(PersonName.of("Test User"))
            .email(Email.of("test@example.com"))
            .login(Username.of("testuser"))
            .password("encrypted123")
            .role(List.of(RolesEnum.CLIENT))
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();

        addressDomain = AddressDomain.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .street("Test Street")
            .number("123")
            .complement("Apt 1")
            .neighborhood("Test Neighborhood")
            .city("Test City")
            .zipCode("12345-678")
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of(addressDomain));

        // When
        UserResponse response = findUserByIdUseCase.findById(userId);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Test User", response.name());
        assertEquals("test@example.com", response.email());
        assertEquals("testuser", response.login());
        assertEquals(List.of(RolesEnum.CLIENT), response.roles());
        assertNotNull(response.addresses());
        assertEquals(1, response.addresses().size());

        verify(userRepository).findById(userId);
        verify(addressRepository).findByAddressFromUser(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            findUserByIdUseCase.findById(userId);
        });

        assertNotNull(exception);
        verify(userRepository).findById(userId);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void shouldFindUserWithEmptyAddressList() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of());

        // When
        UserResponse response = findUserByIdUseCase.findById(userId);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertNotNull(response.addresses());
        assertTrue(response.addresses().isEmpty());

        verify(userRepository).findById(userId);
        verify(addressRepository).findByAddressFromUser(userId);
    }

    @Test
    void shouldFindUserWithMultipleAddresses() {
        // Given
        AddressDomain secondAddress = AddressDomain.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .street("Second Street")
            .number("456")
            .complement("Apt 2")
            .neighborhood("Second Neighborhood")
            .city("Second City")
            .zipCode("98765-432")
            .createdAt(LocalDateTime.now())
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of(addressDomain, secondAddress));

        // When
        UserResponse response = findUserByIdUseCase.findById(userId);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertNotNull(response.addresses());
        assertEquals(2, response.addresses().size());

        verify(userRepository).findById(userId);
        verify(addressRepository).findByAddressFromUser(userId);
    }
}