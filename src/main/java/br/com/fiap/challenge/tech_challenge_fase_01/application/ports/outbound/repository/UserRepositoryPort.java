package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public interface UserRepositoryPort {

    UserDomain save(UserDomain user);

    Optional<UserDomain> findById(UUID id);

    List<UserDomain> findByName(String name);

    Optional<UserDomain> findByLogin(String login);

    void delete(UUID id);

    boolean existsByUsername(String username);

    Optional<UserDomain> findByEmail(String email);

}