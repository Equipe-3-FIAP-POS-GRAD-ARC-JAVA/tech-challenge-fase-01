package br.com.fiap.challenge.tech_challenge_fase_01.application.service.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequestDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
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
    public UserResponseDTOPorts create(UserCreateRequestDTOPorts userCreateRequestDTO) {
        return createUserUseCase.create(userCreateRequestDTO);
    }

    @Override
    public UserResponseDTOPorts createOwner(UserCreateRequestDTOPorts userCreateRequestDTO) {
        return createOwnerUseCase.createOwner(userCreateRequestDTO);
    }

    @Override
    public void delete(String id) {
        deleteUserUseCase.delete(id);
    }

    @Override
    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO) {
        return updateUserUseCase.update(id, userUpdateRequestDTO);
    }

    @Override
    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO) {
        return updatePasswordUseCase.updatePassword(id, updatePasswordRequestDTO);
    }

    @Override
    public List<UserResponseDTOPorts> findByName(String name) {
        return findByNameUseCase.findByName(name);
    }

    @Override
    public UserResponseDTOPorts findById(String id) {
        return findByIdUseCase.findById(id);
    }

}
