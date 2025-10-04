package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

public interface UserRepositoryPort {

    UserDomain createClient(UserDomain user);
    UserDomain update(String id, UserDomain user);
    UserDomain updatePassword(String id, String password);
    List<UserDomain> findByName(String name);
    void delete(String id);
    UserDomain createOwner(UserDomain request);
    UserDomain findByUsername(String username);

}
