package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindUserByNameUseCase Tests")
class FindUserByNameUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private FindUserByNameUseCase findUserByNameUseCase;

    private UserDomain userDomain;

    @BeforeEach
    void setUp() {
        userDomain = UserDomain.builder()
                .id(UUID.randomUUID())
                .name(PersonName.of("João Silva"))
                .login(Username.of("joao.silva"))
                .email(Email.of("joao@email.com"))
                .password("encodedPassword123")
                .role(List.of(RolesEnum.CLIENT))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should find users by name successfully")
    void shouldFindUsersByNameSuccessfully() {
        // Given
        String nameQuery = "João";
        List<UserDomain> users = List.of(userDomain);

        when(userRepository.findByName(anyString())).thenReturn(users);
        when(addressRepository.findByAddressFromUser(userDomain.getId())).thenReturn(List.of());

        // When
        List<UserResponse> result = findUserByNameUseCase.findByName(nameQuery);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).name());
        assertEquals("joao@email.com", result.get(0).email());
    }

    @Test
    @DisplayName("Should return empty list when no users found")
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Given
        String nameQuery = "NonExistentUser";
        List<UserDomain> emptyUsers = List.of();

        when(userRepository.findByName(anyString())).thenReturn(emptyUsers);

        // When
        List<UserResponse> result = findUserByNameUseCase.findByName(nameQuery);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find multiple users with partial name match")
    void shouldFindMultipleUsersWithPartialNameMatch() {
        // Given
        String nameQuery = "Silva";
        
        UserDomain secondUser = UserDomain.builder()
                .id(UUID.randomUUID())
                .name(PersonName.of("Maria Silva"))
                .login(Username.of("maria.silva"))
                .email(Email.of("maria@email.com"))
                .password("encodedPassword123")
                .role(List.of(RolesEnum.CLIENT))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<UserDomain> users = List.of(userDomain, secondUser);

        when(userRepository.findByName(anyString())).thenReturn(users);
        when(addressRepository.findByAddressFromUser(userDomain.getId())).thenReturn(List.of());
        when(addressRepository.findByAddressFromUser(secondUser.getId())).thenReturn(List.of());

        // When
        List<UserResponse> result = findUserByNameUseCase.findByName(nameQuery);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(user -> user.name().contains("João")));
        assertTrue(result.stream().anyMatch(user -> user.name().contains("Maria")));
    }
}