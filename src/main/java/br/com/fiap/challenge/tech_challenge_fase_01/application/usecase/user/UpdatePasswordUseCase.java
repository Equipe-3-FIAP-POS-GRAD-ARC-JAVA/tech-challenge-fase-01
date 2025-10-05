package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

/**
 * Use Case para atualização de senha do usuário.
 * 
 * Responsabilidades:
 * - Orquestrar a alteração de senha
 * - Buscar usuário existente
 * - Criptografar nova senha
 * - Delegar alteração para o método de comportamento do Domain
 */
public class UpdatePasswordUseCase implements UserUpdatePasswordPort {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdatePasswordUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse updatePassword(String id, UpdatePasswordRequest updatePasswordRequest) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));

        // Criptografa a nova senha
        String encryptedPassword = passwordEncoder.encode(updatePasswordRequest.password());

        user.changePassword(encryptedPassword);

        var updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}
