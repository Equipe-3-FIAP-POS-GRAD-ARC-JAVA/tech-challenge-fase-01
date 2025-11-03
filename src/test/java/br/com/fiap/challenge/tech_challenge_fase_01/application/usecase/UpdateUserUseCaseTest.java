package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdateUserUseCase;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateUserUseCaseTest {

    @Test
    void shouldThrowWhenUserNotFound(){
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        UpdateUserUseCase uc = new UpdateUserUseCase(repo);

        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> uc.update(id, new UserUpdateRequest("A","a@a.com","a")));
    }
}
