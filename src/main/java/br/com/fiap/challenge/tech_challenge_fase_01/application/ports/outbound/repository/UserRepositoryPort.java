package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;

public interface UserRepositoryPort {

    UserResponse createClient(UserDomain user);

    UserResponse update(String id, UserDomain user);

    UserResponse updatePassword(String id, String password);

    List<UserResponse> findByName(String name);

    UserResponse findById(String id);

    UserDomain findByIdtoDomain(String id);

    void delete(String id);

    UserResponse createOwner(UserCreateOwnerPort request);

    UserResponse findByUsername(String username);

}
