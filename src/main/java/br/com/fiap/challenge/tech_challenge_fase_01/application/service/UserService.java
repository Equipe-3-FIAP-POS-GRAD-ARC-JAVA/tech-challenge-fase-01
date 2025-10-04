package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
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
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UserService implements
        UserCreatePort,
        UserCreateOwnerPort,
        UserDeletePort,
        UserUpdatePort,
        UserUpdatePasswordPort,
        UserFindByNamePort,
        UserFindByIdPort {

    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTOPorts create(UserCreateRequestDTOPorts userCreateRequestDTO) {
        var user = userCreateRequestDTO.toDomain(userCreateRequestDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRole(List.of(RolesEnum.CLIENT));
        return userRepository.createClient(user);
    }

    @Override
    public UserResponseDTOPorts createOwner(UserCreateRequestDTOPorts userCreateRequestDTO) {
        var user = userCreateRequestDTO.toDomain(userCreateRequestDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRole(List.of(RolesEnum.OWNER));
        return userRepository.createClient(user);
    }

    @Override
    public void delete(String id) {
        userRepository.delete(id);
    }

    @Override
    public UserResponseDTOPorts update(String id, UserUpdateRequestDTOPorts userUpdateRequestDTO) {
        var user = userRepository.findByIdtoDomain(id);
        user.setName(userUpdateRequestDTO.name());
        user.setEmail(userUpdateRequestDTO.email());
        user.setLogin(userUpdateRequestDTO.login());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.update(id, user);
    }

    @Override
    public UserResponseDTOPorts updatePassword(String id, UpdatePasswordRequestDTOPorts updatePasswordRequestDTO) {
        var user = userRepository.findByIdtoDomain(id);
        user.setPassword(updatePasswordRequestDTO.password());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.update(id, user);
    }

    @Override
    public List<UserResponseDTOPorts> findByName(String name) {
        return this.userRepository.findByName(name);
    }

    @Override
    public UserResponseDTOPorts findById(String id) {
        return this.userRepository.findById(id);
    }

}
