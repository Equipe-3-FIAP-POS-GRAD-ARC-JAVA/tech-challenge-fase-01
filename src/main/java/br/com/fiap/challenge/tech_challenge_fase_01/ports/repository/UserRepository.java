package br.com.fiap.challenge.tech_challenge_fase_01.ports.repository;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

import java.util.List;

public interface UserRepository {

    User createClient(User user);
    User update(String id, User user);
    User updatePassword(String id, String password);
    List<User> findByName(String name);
    void delete(String id);
    User createOwner(User request);
    User findByUsername(String username);

}
