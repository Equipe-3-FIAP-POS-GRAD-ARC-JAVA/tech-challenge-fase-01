package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.challenge.tech_challenge_fase_01.application.usecases.UserUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCases {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO create(UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public UserResponseDTO update(String id, UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public UserResponseDTO updatePassword(String id, String password) {
        log.info("Updating password for user id: {}", id);
        var userEntity = this.userRepository.updatePassword(id, password);
        return UserResponseDTO.from(userEntity.toUserDomain());
    }

    @Override
    public List<UserResponseDTO> findByName(String name) {
        log.info("Finding user by name: {}", name);
        return this.userRepository.findByName(name)
            .stream()
            .map(userEntity -> {
                var domain = userEntity.toUserDomain();
                return UserResponseDTO.from(domain);
            })
            .toList();
    }

    @Override
    public void delete(String id) {
        log.info("Deleting user by id: {}", id);
        this.userRepository.delete(id);
    }

}
