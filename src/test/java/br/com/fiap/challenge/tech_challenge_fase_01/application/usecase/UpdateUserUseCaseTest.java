package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdateUserUseCase;

class UpdateUserUseCaseTest {

    @Test
    void shouldThrowWhenUserNotFound(){
        UserRepositoryPort userRepo = mock(UserRepositoryPort.class);
        br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort addressRepo = mock(br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort.class);
        UpdateUserUseCase uc = new UpdateUserUseCase(userRepo, addressRepo);

        UUID id = UUID.randomUUID();
        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> uc.update(id, new UserUpdateRequest("A","a@a.com","a")));
    }
}
