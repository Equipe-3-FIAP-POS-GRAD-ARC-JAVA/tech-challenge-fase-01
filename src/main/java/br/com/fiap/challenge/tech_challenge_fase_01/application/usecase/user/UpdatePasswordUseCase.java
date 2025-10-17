package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

public class UpdatePasswordUseCase implements UserUpdatePasswordPort {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UpdatePasswordUseCase(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse updatePassword(UUID id, UpdatePasswordRequest updatePasswordRequest) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));

        if (!passwordEncoder.matches(updatePasswordRequest.currentPassword(), user.getPassword())) {
            throw new BusinessRuleException("A senha atual está incorreta.");
        }

        if (updatePasswordRequest.currentPassword().equals(updatePasswordRequest.newPassword())) {
            throw new BusinessRuleException("A nova senha não pode ser igual à senha atual.");
        }

        if (!updatePasswordRequest.newPassword().equals(updatePasswordRequest.confirmPassword())) {
            throw new BusinessRuleException("A nova senha e a confirmação não conferem.");
        }

        String encryptedPassword = passwordEncoder.encode(updatePasswordRequest.newPassword());

        user.changePassword(encryptedPassword);

        var updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}