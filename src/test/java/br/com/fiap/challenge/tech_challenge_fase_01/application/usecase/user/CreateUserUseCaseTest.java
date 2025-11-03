package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
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
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private UserCreateRequest request;
    private String name = "Test User";
    private String email = "test@example.com";
    private String login = "testuser";
    private String password = "password123";
    private String encryptedPassword = "encrypted123";
    private String street = "Test Street";
    private String number = "123";
    private String complement = "Apt 1";
    private String neighborhood = "Test Neighborhood";
    private String city = "Test City";
    private String zipCode = "12345-678";

    @BeforeEach
    void setUp() {
        request = new UserCreateRequest(name, email, login, password, street, number, complement, neighborhood, city, zipCode);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();
        UserDomain savedUser = UserDomain.builder()
            .id(userId)
            .name(PersonName.of(name))
            .email(Email.of(email))
            .login(Username.of(login))
            .password(encryptedPassword)
            .role(List.of(RolesEnum.CLIENT))
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();

        AddressDomain savedAddress = AddressDomain.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .street(street)
            .number(number)
            .complement(complement)
            .neighborhood(neighborhood)
            .city(city)
            .zipCode(zipCode)
            .createdAt(LocalDateTime.now())
            .build();

        doNothing().when(userDomainService).ensureUsernameIsUnique(login);
        doNothing().when(userDomainService).ensureEmailIsUnique(any(Email.class));
        when(passwordEncoder.encode(password)).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserDomain.class))).thenReturn(savedUser);
        when(addressRepository.save(any(AddressDomain.class))).thenReturn(savedAddress);
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of(savedAddress));

        // When
        UserResponse response = createUserUseCase.create(request);

        // Then
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals(name, response.name());
        assertEquals(email, response.email());
        assertEquals(login, response.login());
        assertEquals(List.of(RolesEnum.CLIENT), response.roles());
        assertNotNull(response.addresses());
        assertEquals(1, response.addresses().size());

        verify(userDomainService).ensureUsernameIsUnique(login);
        verify(userDomainService).ensureEmailIsUnique(any(Email.class));
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(UserDomain.class));
        verify(addressRepository).save(any(AddressDomain.class));
        verify(addressRepository).findByAddressFromUser(userId);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        String errorMessage = "Username already exists";
        doThrow(new InvalidFieldException("username", errorMessage)).when(userDomainService).ensureUsernameIsUnique(login);

        // When & Then
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> {
            createUserUseCase.create(request);
        });

        assertTrue(exception.getMessage().contains(errorMessage));
        verify(userDomainService).ensureUsernameIsUnique(login);
        verifyNoMoreInteractions(userDomainService, passwordEncoder, userRepository, addressRepository);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        String errorMessage = "Email already exists";
        doNothing().when(userDomainService).ensureUsernameIsUnique(login);
        doThrow(new InvalidFieldException("email", errorMessage)).when(userDomainService).ensureEmailIsUnique(any(Email.class));

        // When & Then
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> {
            createUserUseCase.create(request);
        });

        assertTrue(exception.getMessage().contains(errorMessage));
        verify(userDomainService).ensureUsernameIsUnique(login);
        verify(userDomainService).ensureEmailIsUnique(any(Email.class));
        verifyNoMoreInteractions(userDomainService, passwordEncoder, userRepository, addressRepository);
    }
}