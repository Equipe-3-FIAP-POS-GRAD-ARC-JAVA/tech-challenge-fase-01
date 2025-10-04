package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponseDTOPorts;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;

public interface UserRepositoryPort {

    UserResponseDTOPorts createClient(UserDomain user);

    UserResponseDTOPorts update(String id, UserDomain user);

    UserResponseDTOPorts updatePassword(String id, String password);

    List<UserResponseDTOPorts> findByName(String name);

    UserResponseDTOPorts findById(String id);

    UserDomain findByIdtoDomain(String id);

    void delete(String id);

    UserResponseDTOPorts createOwner(UserCreateOwnerPort request);

    UserResponseDTOPorts findByUsername(String username);

}
