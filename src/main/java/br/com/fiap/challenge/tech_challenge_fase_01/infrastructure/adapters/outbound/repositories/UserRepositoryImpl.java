package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.mappers.UserEntityMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryPort {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userMapper;

    @Override
    public UserDomain save(UserDomain user) {
        return userMapper.toDomain(jpaUserRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public Optional<UserDomain> findById(UUID id) {
        return jpaUserRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return executeWithExceptionHandling(() -> jpaUserRepository.findByLoginIgnoreCase(username).isPresent(), false,
                "checking if username exists: " + username);
    }

    @Override
    public List<UserDomain> findByName(String name) {
        List<JpaUserEntity> entities = this.jpaUserRepository.findByName(name);
        if (entities == null || entities.isEmpty()) {
            throw new UserNotFoundException("User not found by name: " + name);
        }
        return entities.stream().map(userMapper::toDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        JpaUserEntity entity = this.jpaUserRepository.findById(id).orElseThrow(createUserNotFoundExceptionSupplier(id));

        entity.setActive(false);
        this.jpaUserRepository.save(entity);
    }

    public UserDomain createOwner(UserDomain request) {
        JpaUserEntity entity = createEntityFromDomain(request, RolesEnum.OWNER);
        JpaUserEntity saved = this.jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<UserDomain> findByLogin(String login) {
        Optional<UserDomain> result = executeWithExceptionHandling(() -> {
            Optional<JpaUserEntity> entity = jpaUserRepository.findByLoginIgnoreCase(login);
            return entity.map(userMapper::toDomain);
        }, Optional.empty(), "finding user by username: " + login);

        return result;
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return executeWithExceptionHandling(
                () -> jpaUserRepository.findByEmailIgnoreCase(email).map(userMapper::toDomain), Optional.empty(),
                "finding user by email: " + email);
    }

    private <T> T executeWithExceptionHandling(Supplier<T> operation, T defaultValue, String operationDescription) {
        try {
            return operation.get();
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private Supplier<UserNotFoundException> createUserNotFoundExceptionSupplier(UUID id) {
        return () -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + " with id: " + id);
    }

    private JpaUserEntity createEntityFromDomain(UserDomain domain, RolesEnum role) {
        LocalDateTime now = LocalDateTime.now();

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(null);
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setCreatedAt(now);
        entity.setActive(true);
        entity.setRole(List.of(role));

        return entity;
    }
}