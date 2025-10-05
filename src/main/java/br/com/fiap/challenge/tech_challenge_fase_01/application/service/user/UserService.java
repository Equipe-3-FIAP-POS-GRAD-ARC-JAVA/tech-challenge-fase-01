package br.com.fiap.challenge.tech_challenge_fase_01.application.service.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;

/**
 * Facade pattern: Classe de conveniência que agrega os Use Cases de usuário.
 * Esta classe NÃO contém lógica de negócio, apenas delega para os Use Cases
 * específicos.
 * 
 * Vantagem: Simplifica a injeção de dependências na camada de infraestrutura,
 * permitindo injetar uma única classe ao invés de múltiplos Use Cases.
 * 
 * Princípio seguido: Single Responsibility - cada Use Case tem sua própria
 * responsabilidade,
 * esta classe apenas agrega/orquestra chamadas.
 */
public class UserService implements
        UserCreatePort,
        UserCreateOwnerPort,
        UserDeletePort,
        UserUpdatePort,
        UserUpdatePasswordPort,
        UserFindByNamePort,
        UserFindByIdPort {

    private final UserCreatePort createUserUseCase;
    private final UserCreateOwnerPort createOwnerUseCase;
    private final UserDeletePort deleteUserUseCase;
    private final UserUpdatePort updateUserUseCase;
    private final UserUpdatePasswordPort updatePasswordUseCase;
    private final UserFindByNamePort findByNameUseCase;
    private final UserFindByIdPort findByIdUseCase;

    public UserService(
            UserCreatePort createUserUseCase,
            UserCreateOwnerPort createOwnerUseCase,
            UserDeletePort deleteUserUseCase,
            UserUpdatePort updateUserUseCase,
            UserUpdatePasswordPort updatePasswordUseCase,
            UserFindByNamePort findByNameUseCase,
            UserFindByIdPort findByIdUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.createOwnerUseCase = createOwnerUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.findByNameUseCase = findByNameUseCase;
        this.findByIdUseCase = findByIdUseCase;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        return createUserUseCase.create(userCreateRequest);
    }

    @Override
    public UserResponse createOwner(UserCreateRequest userCreateRequest) {
        return createOwnerUseCase.createOwner(userCreateRequest);
    }

    @Override
    public void delete(String id) {
        deleteUserUseCase.delete(id);
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        return updateUserUseCase.update(id, userUpdateRequest);
    }

    @Override
    public UserResponse updatePassword(String id, UpdatePasswordRequest updatePasswordRequest) {
        return updatePasswordUseCase.updatePassword(id, updatePasswordRequest);
    }

    @Override
    public List<UserResponse> findByName(String name) {
        return findByNameUseCase.findByName(name);
    }

    @Override
    public UserResponse findById(String id) {
        return findByIdUseCase.findById(id);
    }

}
