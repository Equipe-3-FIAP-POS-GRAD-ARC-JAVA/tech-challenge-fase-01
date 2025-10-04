package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.output.repository;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;

public interface UserRepositoryPort {

    UserModel createClient(UserModel user);
    UserModel update(String id, UserModel user);
    UserModel updatePassword(String id, String password);
    List<UserModel> findByName(String name);
    void delete(String id);
    UserModel createOwner(UserModel request);
    UserModel findByUsername(String username);

}
