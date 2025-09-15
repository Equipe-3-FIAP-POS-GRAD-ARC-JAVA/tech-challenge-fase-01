package br.com.fiap.challenge.tech_challenge_fase_01.domain.user;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;

public interface UserRepository {

    JpaUserEntity create(UserRequestDTO request);
    JpaUserEntity update(String id, UserRequestDTO request);
    JpaUserEntity updatePassword(String id, String password);
    List<JpaUserEntity> findByName(String name);
    void delete(String id);

}
