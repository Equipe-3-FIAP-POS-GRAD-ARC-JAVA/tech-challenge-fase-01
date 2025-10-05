package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserAlreadyExistsException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

/**
 * Use Case para criação de usuário cliente.
 * 
 * Responsabilidades:
 * - Orquestrar a criação do usuário
 * - Validar se usuário já existe (regra de aplicação)
 * - Delegar criação para o Domain (que contém as regras de negócio)
 */
public class CreateUserUseCase implements UserCreatePort {

    private final UserRepositoryPort userRepository;

    public CreateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {

        if (userRepository.existsByUsername(userCreateRequest.login())) {
            throw new UserAlreadyExistsException(
                    "Já existe um usuário com o login: " + userCreateRequest.login());
        }

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
