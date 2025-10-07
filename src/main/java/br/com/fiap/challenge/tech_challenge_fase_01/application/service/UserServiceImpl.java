package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.repository.UserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserUseCases {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createClient(User user) {
        log.info("Creating user CLIENT with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        user.setRoles(List.of(RolesEnum.CLIENT));
        return userRepository.createClient(user);
    }

    @Override
    public User update(String id, User user) {
        log.info("Updating user with id: {}", id);
        return userRepository.update(id, user);
    }

    @Override
    public User updatePassword(String id, String password) {
        log.info("Updating password for user id: {}", id);
        return userRepository.updatePassword(id, password);
    }

    @Override
    public List<User> findByName(String name) {
        log.info("Finding user by name: {}", name);
        return this.userRepository.findByName(name);
    }

    @Override
    public void delete(String id) {
        log.info("Deleting user by id: {}", id);
        this.userRepository.delete(id);
    }

    @Override
    public User createOwner(User user) {
        log.info("Creating user OWNER with email: {}", user.getEmail());
        return this.userRepository.createOwner(user);
    }

    @Override
    public User findByUsername(String username) {
        log.info("Searching for a singles user with username: {}", username);
        return this.userRepository.findByUsername(username);
    }

}
