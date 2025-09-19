package br.com.fiap.challenge.tech_challenge_fase_01.ports.repository;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;

public interface UserRepository {

    JpaUserEntity create(UserRequestDTO request);
    JpaUserEntity update(String id, UserRequestDTO request);
    JpaUserEntity updatePassword(String id, String password);
    List<JpaUserEntity> findByName(String name);
    void delete(String id);
    User findByEmail(String email);
    User findByLogin(String login);

}
