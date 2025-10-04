package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.repositories;


import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.enumx.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions.NotFoundException;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    private final UserMapper userMapper;

    @Override
    public User createClient(User user) {
        return userMapper.toDomain(jpaUserRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public User update(String id, User user) {
        return this.jpaUserRepository.findById(UUID.fromString(id))
                .map(entity -> {
                    entity.setName(user.getName());
                    entity.setEmail(user.getEmail());
                    entity.setLogin(user.getLogin());
                    JpaUserEntity savedEntity = this.jpaUserRepository.save(entity);
                    return userMapper.toDomain(savedEntity);
                })
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public User updatePassword(String id, String password) {
        return this.jpaUserRepository.findById(UUID.fromString(id))
                .map(entity -> {
                    entity.setPassword(password);
                    JpaUserEntity saved = this.jpaUserRepository.save(entity);
                    return userMapper.toDomain(saved);
                })
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public List<User> findByName(String name) {
        List<JpaUserEntity> entities = this.jpaUserRepository.findByName(name);
        if (entities == null || entities.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User not found by name.");
        }
        return entities.stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        JpaUserEntity entity = this.jpaUserRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));

        entity.setActive(false);
        this.jpaUserRepository.save(entity);
    }

    @Override
    public User createOwner(User request) {
        LocalDateTime now = LocalDateTime.now();

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(null);
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setLogin(request.getLogin());

        entity.setPassword(request.getPassword());

        entity.setCreatedAt(now);
        entity.setActive(true);
        entity.setRoles(List.of(RolesEnum.OWNER));

        JpaUserEntity saved = this.jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public User findByUsername(String login) {
        return jpaUserRepository.findByLogin(login)
                .map(userMapper::toDomain)
                .orElseThrow(() ->
                        new NotFoundException(HttpStatus.NOT_FOUND, "User not found by login: " + login)
                );
    }

}
