package br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;

import java.util.List;

public interface UserUseCases {
    
    User createClient(User user);
    User update(String id, User user);
    User updatePassword(String id, String password);
    List<User> findByName(String name);
    void delete(String id);
    User createOwner(User user);
    User findByUsername(String username);

}
