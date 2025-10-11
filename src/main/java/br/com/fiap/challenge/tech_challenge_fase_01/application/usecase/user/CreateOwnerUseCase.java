package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

/**
 * Use Case para criação de usuário proprietário.
 * 
 * Responsabilidades:
 * - Orquestrar a criação do usuário
 * - Delegar validações para o Domain Service
 * - Criptografar senha antes de passar para o Domain
 * - Delegar criação para o Domain (que contém as regras de negócio)
 */
public class CreateOwnerUseCase implements UserCreateOwnerPort {

    private final UserRepositoryPort userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoderPort passwordEncoder;

    public CreateOwnerUseCase(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createOwner(UserCreateRequest userCreateRequest) {

        // Domain Service valida regras de negócio que envolvem o repositório
        userDomainService.ensureUsernameIsUnique(userCreateRequest.login());
        userDomainService.ensureEmailIsUnique(Email.of(userCreateRequest.email()));

        // Criptografa a senha antes de criar o domínio
        String encryptedPassword = passwordEncoder.encode(userCreateRequest.password());

        // Domain cria e valida a entidade
        var user = UserDomain.createOwner(
                userCreateRequest.name(),
                userCreateRequest.email(),
                userCreateRequest.login(),
                encryptedPassword);

        var savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}
