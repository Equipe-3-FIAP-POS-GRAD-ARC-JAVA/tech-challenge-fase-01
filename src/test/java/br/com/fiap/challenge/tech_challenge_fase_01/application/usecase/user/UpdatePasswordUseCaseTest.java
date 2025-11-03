package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePasswordUseCase Tests")
class UpdatePasswordUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private UpdatePasswordUseCase updatePasswordUseCase;

    private UserDomain userDomain;
    private UUID userId;
    private UpdatePasswordRequest updatePasswordRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        userDomain = UserDomain.builder()
                .id(userId)
                .name(PersonName.of("João Silva"))
                .login(Username.of("joao.silva"))
                .email(Email.of("joao@email.com"))
                .password("encodedCurrentPassword123")
                .role(List.of(RolesEnum.CLIENT))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        updatePasswordRequest = new UpdatePasswordRequest(
                "currentPassword123",
                "newPassword123",
                "newPassword123"
        );
    }

    @Test
    @DisplayName("Should update password successfully")
    void shouldUpdatePasswordSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(passwordEncoder.matches("currentPassword123", "encodedCurrentPassword123")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword123");
        when(userRepository.save(any(UserDomain.class))).thenReturn(userDomain);
        when(addressRepository.findByAddressFromUser(userId)).thenReturn(List.of());

        // When
        UserResponse result = updatePasswordUseCase.updatePassword(userId, updatePasswordRequest);

        // Then
        assertNotNull(result);
        assertEquals("João Silva", result.name());
        assertEquals("joao@email.com", result.email());
        
        verify(passwordEncoder).matches("currentPassword123", "encodedCurrentPassword123");
        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(any(UserDomain.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> updatePasswordUseCase.updatePassword(userId, updatePasswordRequest)
        );
        
        assertEquals("Usuário não encontrado com ID: " + userId, exception.getMessage());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(passwordEncoder.matches("currentPassword123", "encodedCurrentPassword123")).thenReturn(false);

        // When & Then
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> updatePasswordUseCase.updatePassword(userId, updatePasswordRequest)
        );
        
        assertEquals("A senha atual está incorreta.", exception.getMessage());
        verify(passwordEncoder).matches("currentPassword123", "encodedCurrentPassword123");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when new password equals current password")
    void shouldThrowExceptionWhenNewPasswordEqualsCurrentPassword() {
        // Given
        UpdatePasswordRequest samePasswordRequest = new UpdatePasswordRequest(
                "currentPassword123",
                "currentPassword123",
                "currentPassword123"
        );
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(passwordEncoder.matches("currentPassword123", "encodedCurrentPassword123")).thenReturn(true);

        // When & Then
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> updatePasswordUseCase.updatePassword(userId, samePasswordRequest)
        );
        
        assertEquals("A nova senha não pode ser igual à senha atual.", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when new password and confirm password do not match")
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        // Given
        UpdatePasswordRequest mismatchRequest = new UpdatePasswordRequest(
                "currentPassword123",
                "newPassword123",
                "differentPassword123"
        );
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDomain));
        when(passwordEncoder.matches("currentPassword123", "encodedCurrentPassword123")).thenReturn(true);

        // When & Then
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> updatePasswordUseCase.updatePassword(userId, mismatchRequest)
        );
        
        assertEquals("A nova senha e a confirmação não conferem.", exception.getMessage());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }
}