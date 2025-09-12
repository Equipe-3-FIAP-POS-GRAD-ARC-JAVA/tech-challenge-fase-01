package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.fiap.challenge.tech_challenge_fase_01.application.usecases.UserUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCases {

    private final UserRepository userRepository;

    @Override
    public User create(UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public User update(String id, UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public User updatePassword(String id, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }

    @Override
    public User findByName(String name) {
        log.info("Finding user by name: {}", name);
        var user = this.userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User not found by name.");
        }

        var userEntity = user.get();
        return User.create(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getLogin(), userEntity.getPassword());
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
