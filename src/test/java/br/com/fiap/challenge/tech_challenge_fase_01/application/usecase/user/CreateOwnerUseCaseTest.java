package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOwnerUseCase Tests")
class CreateOwnerUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    private CreateOwnerUseCase createOwnerUseCase;

    @BeforeEach
    void setUp() {
        createOwnerUseCase = new CreateOwnerUseCase(userRepository, addressRepository, userDomainService, passwordEncoder);
    }

    @Test
    @DisplayName("Should create owner successfully")
    void shouldCreateOwnerSuccessfully() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "João Silva",
                "joao@email.com",
                "joao123",
                "password123",
                "Rua das Flores",
                "123",
                "Apto 1",
                "Centro",
                "São Paulo",
                "01234567"
        );

        UUID userId = UUID.randomUUID();
        UserDomain savedUser = UserDomain.builder()
            .id(userId)
            .name(PersonName.of("João Silva"))
            .email(Email.of("joao@email.com"))
            .login(Username.of("joao123"))
            .password("encodedPassword123")
            .role(List.of(RolesEnum.OWNER))
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();

        AddressDomain savedAddress = AddressDomain.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .street("Rua das Flores")
            .number("123")
            .complement("Apto 1")
            .neighborhood("Centro")
            .city("São Paulo")
            .zipCode("01234567")
            .createdAt(LocalDateTime.now())
            .build();

        doNothing().when(userDomainService).ensureUsernameIsUnique("joao123");
        doNothing().when(userDomainService).ensureEmailIsUnique(Email.of("joao@email.com"));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(UserDomain.class))).thenReturn(savedUser);
        when(addressRepository.save(any(AddressDomain.class))).thenReturn(savedAddress);
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of(savedAddress));

        // Act
        UserResponse result = createOwnerUseCase.createOwner(request);

        // Assert
        assertNotNull(result);
        assertEquals("João Silva", result.name());
        assertEquals("joao@email.com", result.email());
        assertEquals("joao123", result.login());
        assertEquals(List.of(RolesEnum.OWNER), result.roles());
        assertNotNull(result.addresses());
        assertEquals(1, result.addresses().size());

        var addressResponse = result.addresses().get(0);
        assertEquals("Rua das Flores", addressResponse.street());
        assertEquals("123", addressResponse.number());
        assertEquals("Apto 1", addressResponse.complement());

        // Verify interactions
        verify(userDomainService).ensureUsernameIsUnique("joao123");
        verify(userDomainService).ensureEmailIsUnique(Email.of("joao@email.com"));
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(UserDomain.class));
        verify(addressRepository).save(any(AddressDomain.class));
        verify(addressRepository).findByAddressFromUser(userId);
    }

    @Test
    @DisplayName("Should validate username uniqueness")
    void shouldValidateUsernameUniqueness() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "João Silva",
                "joao@email.com",
                "joao123",
                "password123",
                "Rua das Flores",
                "123",
                "Apto 1",
                "Centro",
                "São Paulo",
                "01234567"
        );

        doThrow(new RuntimeException("Username already exists"))
                .when(userDomainService).ensureUsernameIsUnique("joao123");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createOwnerUseCase.createOwner(request);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(userDomainService).ensureUsernameIsUnique("joao123");
        verifyNoInteractions(passwordEncoder, userRepository, addressRepository);
    }

    @Test
    @DisplayName("Should validate email uniqueness")
    void shouldValidateEmailUniqueness() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
                "João Silva",
                "joao@email.com",
                "joao123",
                "password123",
                "Rua das Flores",
                "123",
                "Apto 1",
                "Centro",
                "São Paulo",
                "01234567"
        );

        doThrow(new RuntimeException("Email already exists"))
                .when(userDomainService).ensureEmailIsUnique(Email.of("joao@email.com"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createOwnerUseCase.createOwner(request);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userDomainService).ensureUsernameIsUnique("joao123");
        verify(userDomainService).ensureEmailIsUnique(Email.of("joao@email.com"));
        verifyNoInteractions(passwordEncoder, userRepository, addressRepository);
    }
}