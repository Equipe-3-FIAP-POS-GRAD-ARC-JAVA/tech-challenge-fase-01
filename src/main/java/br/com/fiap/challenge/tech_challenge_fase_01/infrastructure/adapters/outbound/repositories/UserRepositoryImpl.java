package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.enumx.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.mappers.UserEntityMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryPort {

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
        return jpaUserRepository.findByLogin(username).isPresent();
    }

    public UserDomain createClient(UserDomain user) {
        return save(user);
    }

    public UserDomain update(UUID id, UserDomain user) {
        return this.jpaUserRepository.findById(id)
                .map(entity -> {
                    entity.setName(user.getName());
                    entity.setEmail(user.getEmail());
                    entity.setLogin(user.getLogin());
                    JpaUserEntity savedEntity = this.jpaUserRepository.save(entity);
                    return userMapper.toDomain(savedEntity);
                })
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    public UserDomain updatePassword(UUID id, String password) {
        return this.jpaUserRepository.findById(id)
                .map(entity -> {
                    entity.setPassword(password);
                    JpaUserEntity saved = this.jpaUserRepository.save(entity);
                    return userMapper.toDomain(saved);
                })
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public List<UserDomain> findByName(String name) {
        List<JpaUserEntity> entities = this.jpaUserRepository.findByName(name);
        if (entities == null || entities.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User not found by name.");
        }
        return entities.stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        JpaUserEntity entity = this.jpaUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));

        entity.setActive(false);
        this.jpaUserRepository.save(entity);
    }

    public UserDomain createOwner(UserDomain request) {
        LocalDateTime now = LocalDateTime.now();

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(null);
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setLogin(request.getLogin());

        entity.setPassword(request.getPassword());

        entity.setCreatedAt(now);
        entity.setActive(true);
        entity.setRole(List.of(RolesEnum.OWNER));

        JpaUserEntity saved = this.jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<UserDomain> findByUsername(String login) {
        return jpaUserRepository.findByLogin(login).map(userMapper::toDomain);
    }

}
