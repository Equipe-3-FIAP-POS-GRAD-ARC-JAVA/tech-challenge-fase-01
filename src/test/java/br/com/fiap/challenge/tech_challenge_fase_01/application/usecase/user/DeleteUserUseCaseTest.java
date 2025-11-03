package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteUserUseCase Tests")
class DeleteUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        deleteUserUseCase.delete(userId);

        // Then
        verify(userRepository).delete(userId);
    }

    @Test
    @DisplayName("Should call delete with correct parameters")
    void shouldCallDeleteWithCorrectParameters() {
        // Given
        UUID specificUserId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        deleteUserUseCase.delete(specificUserId);

        // Then
        verify(userRepository).delete(specificUserId);
    }
}