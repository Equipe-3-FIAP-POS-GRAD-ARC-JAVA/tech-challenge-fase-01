package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

/**
 * Use Case para criação de usuário cliente.
 * 
 * Responsabilidades:
 * - Orquestrar a criação do usuário
 * - Delegar validações para o Domain Service
 * - Delegar criação para o Domain (que contém as regras de negócio)
 */
public class CreateUserUseCase implements UserCreatePort {

    private final UserRepositoryPort userRepository;
    private final UserDomainService userDomainService;

    public CreateUserUseCase(UserRepositoryPort userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {

        // Domain Service valida regras de negócio que envolvem o repositório
        userDomainService.ensureUsernameIsUnique(userCreateRequest.login());

        // Domain cria e valida a entidade
        var user = UserDomain.createClient(
                userCreateRequest.name(),
                userCreateRequest.email(),
                userCreateRequest.login(),
                userCreateRequest.password() // TODO: Criptografar senha antes de passar
        );

        var savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}
